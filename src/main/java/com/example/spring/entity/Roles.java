package com.example.spring.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
	GUEST,
	USER,
	ADMIN;

	@Override
	public String getAuthority() {

		return "ROLE_" + name();
	}

}
