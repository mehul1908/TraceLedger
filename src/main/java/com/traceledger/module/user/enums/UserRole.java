package com.traceledger.module.user.enums;

import java.util.Set;

public enum UserRole {

	ROLE_RETAILER(Set.of() , "Retailer"),
    ROLE_WHOLESALER(Set.of(ROLE_RETAILER) , "Wholesaler"),
    ROLE_WAREHOUSE(Set.of(ROLE_WHOLESALER) , "Warehouse"),
    ROLE_TRANSPORTER(Set.of() , "Transporter"),
    ROLE_MANUFACTURER(Set.of(ROLE_WAREHOUSE) , "Manufacturer"),
    ROLE_ADMIN(Set.of() , "Admin");

    private final Set<UserRole> allowedNext;

    private String name;
    
    UserRole(Set<UserRole> allowedNext , String name) {
        this.allowedNext = allowedNext;
        this.name = name;
    }

    public boolean canTransferTo(UserRole target) {
        return allowedNext.contains(target);
    }
    
    public String displayName() {
    	return this.name;
    }
}
