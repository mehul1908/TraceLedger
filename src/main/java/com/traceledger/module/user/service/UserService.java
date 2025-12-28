package com.traceledger.module.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.traceledger.module.auth.dto.RegisterUserModel;
import com.traceledger.module.user.entity.User;

import jakarta.validation.Valid;


public interface UserService extends UserDetailsService{

	User getUserById(Integer userId);

	void saveUser(@Valid RegisterUserModel model);

}
