package com.traceledger.module.user.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traceledger.module.auth.dto.RegisterUserModel;
import com.traceledger.module.user.entity.User;
import com.traceledger.module.user.exception.UserAlreadyCreatedException;
import com.traceledger.module.user.exception.UserNotFoundException;
import com.traceledger.module.user.repo.UserRepo;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;
	
	@Override
	// Return the user by their username(or id) override the fn of UserDetailService
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    return userRepo.findByEmail(username)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}


	@Override
	//Return the User by the 'id'
	public User getUserById(Long userId) {
		Optional<User> userOp = userRepo.findById(userId);
		if(userOp.isPresent()) return userOp.get();
		throw new UserNotFoundException(userId);
	}


	@Override
	//Save the User
	public User saveUser(@Valid RegisterUserModel model) {
		Optional<User> userOp = userRepo.findByEmail(model.getEmail());
		if(userOp.isPresent()) {
			log.error("User is already created : " + model.getEmail());
			throw new UserAlreadyCreatedException(model.getEmail());
		}
		
		User savedUser = User.builder().
				name(model.getName()).
				email(model.getEmail()).
				password(model.getHashedPassword()).
				role(model.getRole()).
				phoneNo(model.getPhoneNo()).
				walletAddress(model.getWalletAddress()).
				build();
		
		userRepo.save(savedUser);
		log.info("user is saved");
		return savedUser;
				
	}


	@Override
	public User getUserByEmailId(String email) {
		Optional<User> userOp = userRepo.findByEmail(email);
		if(userOp.isPresent()) return userOp.get();
		throw new UserNotFoundException(email);
	}


}
