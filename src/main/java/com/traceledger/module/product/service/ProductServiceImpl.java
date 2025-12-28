package com.traceledger.module.product.service;

import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.traceledger.exception.UnauthorizedUserException;
import com.traceledger.module.product.dto.ProductRegisterModel;
import com.traceledger.module.product.entity.Product;
import com.traceledger.module.product.enums.ProductStatus;
import com.traceledger.module.product.repo.ProductRepo;
import com.traceledger.module.product.repo.ProductSequenceRepo;
import com.traceledger.module.user.entity.User;
import com.traceledger.module.user.enums.UserRole;
import com.traceledger.util.HashUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private ProductSequenceRepo prodSeqRepo;

	@Override
	@Transactional
	public void createProduct(@Valid ProductRegisterModel model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	    if (auth == null || !(auth.getPrincipal() instanceof User user)) {
	        throw new UnauthorizedUserException("User is unauthenticated");
	    }

	    if (user.getRole() != UserRole.ROLE_MANUFACTURER) {
	        throw new UnauthorizedUserException("Only manufacturers can create products");
	    }

	    prodSeqRepo.insertDummyRow();
	    Long seq = prodSeqRepo.getLastInsertedId();

	    String prodCode = String.format("P%03d", seq);

	    String canonical = String.join("|",
	    	    prodCode,
	    	    model.getName().trim(),
	    	    model.getMrp().setScale(2, RoundingMode.HALF_UP).toString()
	    	);

	    String prodHash = HashUtil.sha256(canonical);
	    
	    Product prod = Product.builder()
	    		.description(model.getDescription())
	    		.mrp(model.getMrp())
	    		.name(model.getName())
	    		.productCode(prodCode)
	    		.productHash(prodHash)
	    		.status(ProductStatus.ACTIVE)
	    		.build();

	    try {
	        productRepo.save(prod);
	    } catch (DataIntegrityViolationException ex) {
	        throw new RuntimeException("Product already exists");
	    }

	    log.info("Product {} created successfully", prodCode);
		
	}

}
