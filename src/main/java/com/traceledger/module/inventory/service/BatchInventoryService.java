package com.traceledger.module.inventory.service;

import com.traceledger.module.inventory.entity.BatchInventory;
import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.user.entity.User;

public interface BatchInventoryService {

	void create(Batch batch, Integer quantity , User performedBy , String metadata);

	BatchInventory findBatchInvByBatchAndOwner(Batch batch, User owner);

	void save(BatchInventory batchInv);

	BatchInventory findBatchInvByBatchAndOwnerIfAbsentCreate(Batch batch, User toUser);

}
