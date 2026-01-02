package com.traceledger.module.production.exception;

public class BatchNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2043106335514227538L;

	public BatchNotFoundException() {
		super("Batch not found");
	}

	public BatchNotFoundException(Long id) {
		super("Batch with id " + id+ " is not found");
	}

	public BatchNotFoundException(String code) {
		super("Batch with code " + code+ " is not found");
	}
}
