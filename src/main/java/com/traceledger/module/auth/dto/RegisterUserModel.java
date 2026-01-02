package com.traceledger.module.auth.dto;

import com.traceledger.module.user.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserModel {
	
	@NotBlank(message = "Name can not be blank" )
	private String name;
	
	@NotBlank(message = "Password can not be blank")
    @Size(min = 8, max = 64)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
        message = "Password must contain upper, lower, number, and special character"
    )
	private String password;
	
	@NotBlank(message = "Email can not be blank")
	@Email(message = "Email must be in proper format")
	private String email;
	
	@NotNull(message="Role can not be null")
	private UserRole role;
	
	@NotBlank(message = "Phone can not be blank")
	@Pattern(
		    regexp = "^\\+[1-9]\\d{7,14}$",
		    message = "Phone number must be in international format, e.g. +919876543210"
		)
	private String phoneNo;
	
	private String hashedPassword;
	
	@NotBlank(message = "Wallet Address can not be null")
	private String walletAddress;
}
