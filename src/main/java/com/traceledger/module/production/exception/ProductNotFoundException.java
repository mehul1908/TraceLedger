package com.traceledger.module.production.exception;

public class ProductNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2043106335514227538L;

	public ProductNotFoundException() {
		super("Product not found");
	}

	public ProductNotFoundException(Long id) {
		super("Product with id " + id+ " is not found");
	}

	public ProductNotFoundException(String code) {
		super("Product with code " + code+ " is not found");
	}
}
