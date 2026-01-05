package com.traceledger.module.user.dto;

import com.traceledger.module.user.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserModel {

	private String name;
	
	private String hashedPassword;
	
	private String email;
	
	private UserRole role;
	
	private String phoneNo;
	
	private String walletAddress;
	
}
