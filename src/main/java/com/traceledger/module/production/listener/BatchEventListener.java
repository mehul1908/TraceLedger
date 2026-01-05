package com.traceledger.module.production.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.traceledger.module.audit.enums.AuditAction;
import com.traceledger.module.audit.service.AuditLogService;
import com.traceledger.module.inventory.service.BatchInventoryService;
import com.traceledger.module.production.record.BatchCreatedEvent;

@Component
public class BatchEventListener {

    @Autowired
    private BatchInventoryService batchInventoryService;

    @Autowired
    private AuditLogService auditLogService;

    @EventListener
    public void handleBatchCreated(BatchCreatedEvent event) {
        batchInventoryService.create(event.batch(), event.batch().getQuantity(), event.user(), "Batch Created");
        auditLogService.create(AuditAction.CREATED, event.user(), "Batch Created: " + event.batch().getBatchNo());
    }
}

