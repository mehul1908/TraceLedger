package com.traceledger.module.user.enums;

public enum UserRole {
	
	ROLE_MANUFACTURER("manufacturer"),
    ROLE_TRANSPORTER("transporter"),
    ROLE_WAREHOUSE("warehouse"),
    ROLE_WHOLESALER("wholesaler"),
    ROLE_RETAILER("retailer"),
    ROLE_ADMIN("admin");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }
    
    public String displayName() {
        return value;
    }
}
