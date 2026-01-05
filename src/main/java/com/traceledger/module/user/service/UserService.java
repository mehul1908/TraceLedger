package com.traceledger.module.user.service;


import com.traceledger.module.auth.dto.RegisterUserModel;
import com.traceledger.module.user.dto.CreateUserModel;
import com.traceledger.module.user.entity.User;

import jakarta.validation.Valid;


public interface UserService{

	User getUserById(Long userId);

	User getUserByEmailId(String email);

	void saveUser(CreateUserModel createModel);

}
