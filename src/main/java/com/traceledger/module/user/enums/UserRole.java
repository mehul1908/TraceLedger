package com.traceledger.module.user.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
	
	ROLE_MANUFACTURER("manufacturer"),
    ROLE_TRANSPORTER("transporter"),
    ROLE_WAREHOUSE("warehouse"),
    ROLE_WHOLESALER("wholesaler"),
    ROLE_RETAILER("retailer");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
