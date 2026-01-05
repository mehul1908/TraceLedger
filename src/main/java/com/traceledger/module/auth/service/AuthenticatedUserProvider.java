package com.traceledger.module.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.traceledger.exception.UnauthorizedUserException;
import com.traceledger.module.auth.entity.SecurityUser;
import com.traceledger.module.user.entity.User;

@Component
public class AuthenticatedUserProvider {

    public User getAuthenticatedUser() {
    	System.out.println("In get Authenticated User");
    	Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
            || "anonymousUser".equals(auth.getPrincipal())) {
            throw new UnauthorizedUserException("User is unauthenticated");
        }
        
        SecurityUser secUser = (SecurityUser) auth.getPrincipal();
        return secUser.getUser();
    }
}

