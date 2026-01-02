package com.traceledger.module.audit.service;

import com.traceledger.module.audit.enums.AuditAction;
import com.traceledger.module.user.entity.User;

public interface AuditLogService {

	void create(AuditAction action, User performedBy, String metadata);

}
