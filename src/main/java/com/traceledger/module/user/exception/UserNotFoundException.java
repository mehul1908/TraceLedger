package com.traceledger.module.user.exception;

public class UserNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2043106335514227538L;

	public UserNotFoundException() {
		super("Incident not found");
	}

	public UserNotFoundException(Long userId) {
		super("User with id " + userId+ " is not found");
	}

	public UserNotFoundException(String email) {
		super("User with email id " + email+ " is not found");
	}
}
