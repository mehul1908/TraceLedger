package com.traceledger.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.traceledger.module.auth.entity.SecurityUser;
import com.traceledger.module.auth.service.BlackListedTokenService;
import com.traceledger.module.user.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtutils;

    @Autowired
    private UserService userService;

    @Autowired
    private BlackListedTokenService tokenService;
    

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String url = request.getRequestURI();
        return url.contains("/auth")
                || url.contains("/user/register")
                || url.contains("/swagger-ui")
                || url.contains("/v3/api-docs")
                || url.contains("/apidocs.html");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("JWT token missing or malformed");
        }

        String jwt = authHeader.substring(7);

        try {
            if (tokenService.isTokenBlackListed(jwt)) {
                throw new BadCredentialsException("Token is blacklisted (user logged out)");
            }

            if (jwtutils.isTokenExpired(jwt)) {
                throw new BadCredentialsException("JWT token has expired");
            }

            if (!jwtutils.validateToken(jwt)) {
                throw new AccessDeniedException("JWT token validation failed");
            }

            String email = jwtutils.extractUserID(jwt);
            SecurityUser user = new SecurityUser(userService.getUserByEmailId(email));

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            throw new BadCredentialsException("JWT token has expired", ex);
        } catch (JwtException | SecurityException ex) {
            throw new AccessDeniedException("Invalid JWT token", ex);
        }
    }
}
