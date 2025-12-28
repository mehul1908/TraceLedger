package com.traceledger.module.auth.service;

public interface BlackListedTokenService {

	boolean isTokenBlackListed(String jwt);

	void logout(String token);
	
	
}
