package com.traceledger.module.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traceledger.dto.ApiResponse;
import com.traceledger.dto.LoginResponse;
import com.traceledger.module.auth.dto.LoginUserModel;
import com.traceledger.module.auth.dto.RegisterUserModel;
import com.traceledger.module.auth.exception.UserIdAndPasswordNotMatchException;
import com.traceledger.module.user.entity.User;
import com.traceledger.module.user.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passEncoder;
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse> saveUser(@RequestBody @Valid RegisterUserModel model){
		String hashedPass = passEncoder.encode(model.getPassword());
		model.setHashedPassword(hashedPass);
		userService.saveUser(model);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, null, "User is created successfully"));
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> loginUser(@RequestBody @Valid LoginUserModel model){
		User user = userService.getUserById(model.getId());
		if(passEncoder.matches(model.getPassword(), user.getPassword())) {
			LoginResponse lr = new LoginResponse(user.getId() , user.getName(), user.getRole().toString());
			return ResponseEntity.ok(new ApiResponse(true , lr , "User is successfully logged in"));
		}
		throw new UserIdAndPasswordNotMatchException();
		
	}
}
