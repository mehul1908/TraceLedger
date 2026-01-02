package com.traceledger.module.audit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.traceledger.module.audit.entity.AuditLog;

public interface AuditLogRepo extends JpaRepository<AuditLog, Long>{

}
