package com.example.config.security;

import com.example.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

	public UserDetailsImpl(User user) {
		super(user.getId(), user.getPassword(), authorities());
	}

	private static Collection<? extends GrantedAuthority> authorities() {
		return AuthorityUtils.createAuthorityList("USER");
	}
}
