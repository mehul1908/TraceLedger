package com.traceledger.module.audit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traceledger.module.audit.entity.AuditLog;
import com.traceledger.module.audit.enums.AuditAction;
import com.traceledger.module.audit.repo.AuditLogRepo;
import com.traceledger.module.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService{

	private final AuditLogRepo auditRepo;
	
	@Override
	public void create(AuditAction action, User performedBy, String metadata) {
		AuditLog audit = AuditLog.builder()
				.action(action)
				.performedBy(performedBy)
				.metadata(metadata)
				.build();
		auditRepo.save(audit);
		
		log.info("Audit saved : " + audit.getId());
	}

}
