package com.traceledger.module.audit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traceledger.module.audit.entity.AuditLog;
import com.traceledger.module.audit.enums.AuditAction;
import com.traceledger.module.audit.repo.AuditLogRepo;
import com.traceledger.module.user.entity.User;


@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService{

	@Autowired
	private AuditLogRepo auditRepo;
	
	@Override
	public void create(AuditAction action, User performedBy, String metadata) {
		AuditLog audit = AuditLog.builder()
				.action(action)
				.performedBy(performedBy)
				.metadata(metadata)
				.build();
		auditRepo.save(audit);
		
	}

}
