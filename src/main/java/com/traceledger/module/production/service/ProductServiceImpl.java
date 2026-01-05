package com.traceledger.module.production.service;

import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traceledger.exception.UnauthorizedUserException;
import com.traceledger.module.audit.enums.AuditAction;
import com.traceledger.module.audit.service.AuditLogService;
import com.traceledger.module.auth.service.AuthenticatedUserProvider;
import com.traceledger.module.auth.service.AuthenticatedUserProvider;
import com.traceledger.module.production.dto.ProductRegisterModel;
import com.traceledger.module.production.entity.Product;
import com.traceledger.module.production.enums.ProductStatus;
import com.traceledger.module.production.exception.ProductNotFoundException;
import com.traceledger.module.production.repo.ProductRepo;
import com.traceledger.module.production.repo.ProductSequenceRepo;
import com.traceledger.module.user.entity.User;
import com.traceledger.module.user.enums.UserRole;
import com.traceledger.util.HashUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final SequenceGeneratorService sequenceService;
    private final HashService hashService;
    private final AuthenticatedUserProvider authUserProvider;
    private final AuditLogService auditService;

    @Override
    public void createProduct(@Valid ProductRegisterModel model) {
        User user = authUserProvider.getAuthenticatedUser();

        if (user.getRole() != UserRole.ROLE_MANUFACTURER) {
            throw new UnauthorizedUserException("Only manufacturers can create products");
        }

        long seq = sequenceService.nextProductSeq();
        String prodCode = String.format("P%03d", seq);
        String prodHash = hashService.generateProductHash(prodCode, model.getName(), model.getMrp());

        Product prod = Product.builder()
                .description(model.getDescription())
                .mrp(model.getMrp())
                .name(model.getName())
                .productCode(prodCode)
                .productHash(prodHash)
                .status(ProductStatus.ACTIVE)
                .build();

        productRepo.save(prod);
        auditService.create(AuditAction.CREATED, user, "Product Created : " + prodCode);
        log.info("Product {} created successfully", prodCode);
    }

    @Override
    public Product findByProductCode(String productCode) {
        return productRepo.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException(productCode));
    }
}
