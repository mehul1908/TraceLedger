package com.traceledger.module.production.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traceledger.exception.UnauthorizedUserException;
import com.traceledger.module.audit.enums.AuditAction;
import com.traceledger.module.audit.service.AuditLogService;
import com.traceledger.module.auth.service.AuthenticatedUserProvider;
import com.traceledger.module.inventory.service.BatchInventoryService;
import com.traceledger.module.production.dto.BatchRegisterModel;
import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.production.entity.Factory;
import com.traceledger.module.production.entity.Product;
import com.traceledger.module.production.exception.BatchNotFoundException;
import com.traceledger.module.production.record.BatchCreatedEvent;
import com.traceledger.module.production.repo.BatchNoSequenceRepo;
import com.traceledger.module.production.repo.BatchRepo;
import com.traceledger.module.user.entity.User;
import com.traceledger.module.user.enums.UserRole;
import com.traceledger.util.HashUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BatchServiceImpl implements BatchService {

    private final BatchRepo batchRepo;
    private final ProductService productService;
    private final FactoryService factoryService;
    private final SequenceGeneratorService sequenceService;
    private final HashService hashService;
    private final AuthenticatedUserProvider authUserProvider;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void createBatch(@Valid BatchRegisterModel model) {
        User user = authUserProvider.getAuthenticatedUser();

        if (user.getRole() != UserRole.ROLE_MANUFACTURER) {
            throw new UnauthorizedUserException("Only manufacturers can create batches");
        }

        Product product = productService.findByProductCode(model.getProductCode());
        Factory factory = factoryService.findByFactoryCode(model.getFactoryCode());

        LocalDate manufactureDate = LocalDate.now();
        int nextSeq = sequenceService.nextBatchSeq(product.getId(), factory.getId(), manufactureDate);

        String datePart = manufactureDate.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String batchNo = String.format("B-%s%s%s%02d", datePart, product.getProductCode(), factory.getFactoryCode(), nextSeq);
        String batchHash = hashService.generateBatchHash(batchNo, product.getProductHash(), factory.getFactoryCode(), manufactureDate);

        Batch batch = Batch.builder()
                .batchNo(batchNo)
                .product(product)
                .factory(factory)
                .manufactureDate(manufactureDate)
                .batchHash(batchHash)
                .quantity(model.getQuantity())
                .build();

        batchRepo.save(batch);

        // Publish event to decouple audit & inventory
        eventPublisher.publishEvent(new BatchCreatedEvent(batch, user));
        log.info("Batch {} created successfully", batchNo);
    }

    @Override
    public Batch getBatchById(Long batchId) {
        return batchRepo.findById(batchId)
                .orElseThrow(() -> new BatchNotFoundException(batchId));
    }

    @Override
    public Optional<Batch> getBatchOptionalByBatchHash(String batchHash) {
        return batchRepo.findByBatchHash(batchHash);
    }
}
