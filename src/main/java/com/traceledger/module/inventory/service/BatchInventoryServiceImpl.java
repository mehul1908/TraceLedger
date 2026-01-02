package com.traceledger.module.inventory.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traceledger.module.audit.enums.AuditAction;
import com.traceledger.module.audit.service.AuditLogService;
import com.traceledger.module.inventory.entity.BatchInventory;
import com.traceledger.module.inventory.exception.BatchInvNotFoundException;
import com.traceledger.module.inventory.repo.BatchInvRepo;
import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.user.entity.User;

@Service
@Transactional
public class BatchInventoryServiceImpl implements BatchInventoryService {
	
	@Autowired
	private BatchInvRepo batchInvRepo;
	
	@Autowired
	private AuditLogService auditService;

	@Override
	public void create(Batch batch, Integer quantity , User performedBy , String metadata) {

		BatchInventory batchInv = BatchInventory.builder()
				.availableQuantity(quantity)
				.batch(batch)
				.owner(performedBy)
				.build();
		
		batchInvRepo.save(batchInv);
		
		auditService.create(AuditAction.OWNERSHIP_TRANSFERRED , performedBy , metadata);
		
	}

	@Override
	public BatchInventory findBatchInvByBatchAndOwner(Batch batch, User owner) {
		Optional<BatchInventory> batchInvOp = batchInvRepo.findByBatchAndOwner(batch , owner);
		if(batchInvOp.isPresent()) return batchInvOp.get();
		throw new BatchInvNotFoundException(batch.getBatchNo(), owner.getEmail());
	}

	@Override
	public void save(BatchInventory batchInv) {
		batchInvRepo.save(batchInv);
		
	}

	@Override
	public BatchInventory findBatchInvByBatchAndOwnerIfAbsentCreate(Batch batch, User owner) {
		Optional<BatchInventory> batchInvOp = batchInvRepo.findByBatchAndOwner(batch , owner);
		if(batchInvOp.isPresent()) return batchInvOp.get();
		BatchInventory batchInv = BatchInventory.builder()
				.availableQuantity(0)
				.batch(batch)
				.owner(owner)
				.reservedQuantity(0)
				.build();
		batchInvRepo.save(batchInv);
		return batchInv;
		
	}

	
	
}
