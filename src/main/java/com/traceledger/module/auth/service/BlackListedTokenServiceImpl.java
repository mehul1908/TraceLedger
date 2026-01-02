package com.traceledger.module.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traceledger.module.auth.entity.BlackListedToken;
import com.traceledger.module.auth.repo.BlackListedTokenRepo;

@Transactional
@Service
public class BlackListedTokenServiceImpl implements BlackListedTokenService{

	@Autowired
	private BlackListedTokenRepo tokenRepo;
	
	@Override
	public boolean isTokenBlackListed(String jwt) {
		Optional<BlackListedToken> token = tokenRepo.findById(jwt);
		return token.isPresent();
	}

	@Override
	
	public void logout(String token) {
		
		BlackListedToken blacklisted = new BlackListedToken(token);
		tokenRepo.save(blacklisted);
		
	}

}
