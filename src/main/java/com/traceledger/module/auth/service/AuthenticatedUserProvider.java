package com.traceledger.module.auth.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.traceledger.exception.UnauthorizedUserException;
import com.traceledger.module.auth.entity.SecurityUser;
import com.traceledger.module.user.entity.User;

@Service
public class AuthenticatedUserProvider {

    public User getAuthenticatedUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser user)) {
            throw new UnauthorizedUserException("User is unauthenticated");
        }
        return user.getUser();
    }
}

