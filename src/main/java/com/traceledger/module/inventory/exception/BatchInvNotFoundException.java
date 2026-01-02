package com.traceledger.module.inventory.exception;

public class BatchInvNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2043106335514227538L;

	public BatchInvNotFoundException() {
		super("Batch Inventory not found");
	}

	public BatchInvNotFoundException(String batchNo , String ownerEmail) {
		super("Batch with batch id " + batchNo+ " and Owner " + ownerEmail);
	}

	
}
