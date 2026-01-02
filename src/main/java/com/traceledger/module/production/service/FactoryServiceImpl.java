package com.traceledger.module.production.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traceledger.exception.UnauthorizedUserException;
import com.traceledger.module.audit.enums.AuditAction;
import com.traceledger.module.audit.service.AuditLogService;
import com.traceledger.module.production.dto.FactoryRegisterModel;
import com.traceledger.module.production.entity.Factory;
import com.traceledger.module.production.enums.FactoryStatus;
import com.traceledger.module.production.exception.FactoryNotFoundException;
import com.traceledger.module.production.repo.FactoryRepo;
import com.traceledger.module.production.repo.FactorySequenceRepo;
import com.traceledger.module.user.entity.User;
import com.traceledger.module.user.enums.UserRole;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class FactoryServiceImpl implements FactoryService {

	@Autowired
	private FactoryRepo factoryRepo;

	@Autowired
	private FactorySequenceRepo factSeqRepo;
	
	@Autowired
	private AuditLogService auditService;

	@Override
	@Transactional
	public void createFactory(@Valid FactoryRegisterModel model) {

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	    if (auth == null || !(auth.getPrincipal() instanceof User user)) {
	        throw new UnauthorizedUserException("User is unauthenticated");
	    }

	    if (user.getRole() != UserRole.ROLE_MANUFACTURER) {
	        throw new UnauthorizedUserException("Only manufacturers can create factories");
	    }

	    factSeqRepo.insertDummyRow();
	    Long seq = factSeqRepo.getLastInsertedId();

	    String factoryCode = String.format("F%03d", seq);

	    Factory factory = Factory.builder()
	        .factoryCode(factoryCode)
	        .name(model.getName())
	        .location(model.getLocation())
	        .manufacturer(user)
	        .status(FactoryStatus.ACTIVE)
	        .build();

	    factoryRepo.save(factory);
	    auditService.create(AuditAction.CREATED, user, "Factory created:"+factoryCode);
	    log.info("Factory {} created successfully", factoryCode);
	}

	
	@Override
	public Factory findByFactoryCode(String factoryCode) {
		Optional<Factory> factoryOp = factoryRepo.findByFactoryCode(factoryCode);
		if(factoryOp.isPresent()) return factoryOp.get();
		throw new FactoryNotFoundException(factoryCode);
	}

}
