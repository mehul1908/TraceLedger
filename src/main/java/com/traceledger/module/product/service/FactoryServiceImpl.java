package com.traceledger.module.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.traceledger.exception.UnauthorizedUserException;
import com.traceledger.module.product.dto.FactoryRegisterModel;
import com.traceledger.module.product.entity.Factory;
import com.traceledger.module.product.enums.FactoryStatus;
import com.traceledger.module.product.repo.FactoryRepo;
import com.traceledger.module.product.repo.FactorySequenceRepo;
import com.traceledger.module.user.entity.User;
import com.traceledger.module.user.enums.UserRole;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FactoryServiceImpl implements FactoryService {

	@Autowired
	private FactoryRepo factoryRepo;

	@Autowired
	private FactorySequenceRepo factSeqRepo;

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

	    log.info("Factory {} created successfully", factoryCode);
	}


}
