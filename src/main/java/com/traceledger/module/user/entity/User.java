package com.traceledger.module.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.traceledger.module.user.enums.UserRole;
import com.traceledger.module.user.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Email
    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Pattern(
        regexp = "^\\+[1-9]\\d{7,14}$",
        message = "Phone number must be in international format"
    )
    @Column(nullable = false)
    private String phoneNo;

    @Pattern(
        regexp = "^0x[a-fA-F0-9]{40}$",
        message = "Invalid Ethereum wallet address"
    )
    @Column(nullable = false, unique = true, length = 42)
    @JsonIgnore
    private String walletAddress;

    
}
