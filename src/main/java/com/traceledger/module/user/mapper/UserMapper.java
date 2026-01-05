package com.traceledger.module.user.mapper;

import com.traceledger.module.auth.dto.RegisterUserModel;
import com.traceledger.module.user.dto.CreateUserModel;

public class UserMapper {

    private UserMapper() {} // Utility class, no instantiation

    public static CreateUserModel toCreateUserModel(RegisterUserModel model, String hashedPassword) {
        return new CreateUserModel(
            model.getName(),
            hashedPassword,
            model.getEmail(),
            model.getRole(),
            model.getPhoneNo(),
            model.getWalletAddress()
        );
    }
}
