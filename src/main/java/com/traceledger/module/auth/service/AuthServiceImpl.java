package com.traceledger.module.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.traceledger.config.JWTUtils;
import com.traceledger.dto.LoginResponse;
import com.traceledger.module.auth.dto.LoginUserModel;
import com.traceledger.module.auth.dto.RegisterUserModel;
import com.traceledger.module.auth.exception.UserIdAndPasswordNotMatchException;
import com.traceledger.module.user.dto.CreateUserModel;
import com.traceledger.module.user.entity.User;
import com.traceledger.module.user.mapper.UserMapper;
import com.traceledger.module.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	private final UserService userService;
	
	private final PasswordEncoder passEncoder;
	
	private final JWTUtils jwtUtils;
	
	@Override
	public void createUser(@Valid RegisterUserModel model) {
		
		String hashedPassword = passEncoder.encode(model.getPassword());
		
		CreateUserModel createModel = UserMapper.toCreateUserModel(model, hashedPassword);
		
		userService.saveUser(createModel);
		
	}

	@Override
	public LoginResponse authenticate(@Valid LoginUserModel model) {
		User user = userService.getUserById(model.getId());
		if(passEncoder.matches(model.getPassword(), user.getPassword())) {
			String token = jwtUtils.generateToken(user.getEmail());
			LoginResponse lr = new LoginResponse(user.getId() , user.getName(), user.getRole().toString() , token);
			log.info("User Log in successfully");
			return lr;
		}
		log.error("Password and Id does not match");
		throw new UserIdAndPasswordNotMatchException();
		
	}

}
