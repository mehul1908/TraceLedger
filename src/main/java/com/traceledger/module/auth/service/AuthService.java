package com.traceledger.module.auth.service;

import com.traceledger.dto.LoginResponse;
import com.traceledger.module.auth.dto.LoginUserModel;
import com.traceledger.module.auth.dto.RegisterUserModel;

import jakarta.validation.Valid;

public interface AuthService {

	void createUser(@Valid RegisterUserModel model);

	LoginResponse authenticate(@Valid LoginUserModel model);

}
