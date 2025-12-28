package com.traceledger.module.user.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1699884767533561612L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer id;
	
	@Column(nullable=false)
	private String name;
	
	@JsonIgnore
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false , unique = true)
	@Email
	private String email;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.ACTIVE;

	@Column(nullable=false)
	@Pattern(
		    regexp = "^\\+[1-9]\\d{7,14}$",
		    message = "Phone number must be in international format, e.g. +919876543210"
		)
	private String phoneNo;
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return this.email;
	}
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
	    return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.status == UserStatus.ACTIVE;
	}
	
}
