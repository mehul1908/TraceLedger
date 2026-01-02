package com.traceledger.module.production.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traceledger.exception.UnauthorizedUserException;
import com.traceledger.module.audit.enums.AuditAction;
import com.traceledger.module.audit.service.AuditLogService;
import com.traceledger.module.inventory.service.BatchInventoryService;
import com.traceledger.module.production.dto.BatchRegisterModel;
import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.production.entity.Factory;
import com.traceledger.module.production.entity.Product;
import com.traceledger.module.production.exception.BatchNotFoundException;
import com.traceledger.module.production.repo.BatchNoSequenceRepo;
import com.traceledger.module.production.repo.BatchRepo;
import com.traceledger.module.user.entity.User;
import com.traceledger.module.user.enums.UserRole;
import com.traceledger.util.HashUtil;

import jakarta.validation.Valid;

@Service
@Transactional
public class BatchServiceImpl implements BatchService {

	@Autowired
	private BatchNoSequenceRepo batchNoRepo;
	
	@Autowired
	private BatchRepo batchRepo;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private FactoryService factoryService;
	
	@Autowired
	private BatchInventoryService batchInvService;
	
	@Autowired
	private AuditLogService auditService;
	
	@Override
	@Transactional
	public void createBatch(@Valid BatchRegisterModel model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	    if (auth == null || !(auth.getPrincipal() instanceof User user)) {
	        throw new UnauthorizedUserException("User is unauthenticated");
	    }
	    
	    if (user.getRole() != UserRole.ROLE_MANUFACTURER) {
	        throw new UnauthorizedUserException("Only manufacturers can create factories");
	    }

	    Product product = productService.findByProductCode(
	            model.getProductCode()
	    );

	    Factory factory = factoryService.findByFactoryCode(
	            model.getFactoryCode()
	    );

	    LocalDate manufactureDate = LocalDate.now();

	    int nextSeq = batchNoRepo.findMaxSeq(
	            manufactureDate,
	            product.getId(),
	            factory.getId()
	    ) + 1;

	    batchNoRepo.insertSeq(
	            manufactureDate,
	            product.getId(),
	            factory.getId(),
	            nextSeq
	    );

	    String datePart = manufactureDate.format(
	            DateTimeFormatter.ofPattern("yyMMdd")
	    );

	    String batchNo = String.format(
	            "B-%s%s%s%02d",
	            datePart,
	            product.getProductCode(),
	            factory.getFactoryCode(),
	            nextSeq
	    );

	    String canonical = String.join("|",
	            batchNo,
	            product.getProductHash(),
	            factory.getFactoryCode(),
	            manufactureDate.toString()
	    );

	    String batchHash = HashUtil.sha256(canonical);

	    Batch batch = Batch.builder()
	            .batchNo(batchNo)
	            .product(product)
	            .factory(factory)
	            .manufactureDate(manufactureDate)
	            .batchHash(batchHash)
	            .quantity(model.getQuantity())
	            .build();

	    batchRepo.save(batch);
	    
	    auditService.create(AuditAction.CREATED, user , "Batch Created" + batchNo);
	    
	    batchInvService.create(batch , batch.getQuantity() , user , "Batch Created (Ownership Created) : " + batchNo);
	    
	    
	}

	@Override
	public Batch getBatchById(Long batchId) {
		Optional<Batch> batchOp = batchRepo.findById(batchId);
		if(batchOp.isPresent()) return batchOp.get();
		throw new BatchNotFoundException(batchId);
	}


}
