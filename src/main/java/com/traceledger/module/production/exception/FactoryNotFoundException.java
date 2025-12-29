package com.traceledger.module.production.exception;

public class FactoryNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2043106335514227538L;

	public FactoryNotFoundException() {
		super("Factory not found");
	}

	public FactoryNotFoundException(Integer id) {
		super("Factory with id " + id+ " is not found");
	}

	public FactoryNotFoundException(String code) {
		super("Factory with code " + code+ " is not found");
	}
}
