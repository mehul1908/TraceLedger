package com.traceledger.module.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.traceledger.module.auth.dto.RegisterUserModel;
import com.traceledger.module.user.entity.User;

import jakarta.validation.Valid;


public interface UserService extends UserDetailsService{

	User getUserById(Long userId);

	User saveUser(@Valid RegisterUserModel model);

	User getUserByEmailId(String email);

}
