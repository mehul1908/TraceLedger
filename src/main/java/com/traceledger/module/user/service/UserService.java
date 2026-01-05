package com.traceledger.module.user.service;


import com.traceledger.module.user.dto.CreateUserModel;
import com.traceledger.module.user.entity.User;


public interface UserService{

	User getUserById(Long userId);

	User getUserByEmailId(String email);

	void saveUser(CreateUserModel createModel);

}
