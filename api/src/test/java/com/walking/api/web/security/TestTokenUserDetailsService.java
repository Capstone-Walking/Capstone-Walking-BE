package com.walking.api.web.security;

import com.walking.api.security.authentication.authority.Roles;
import com.walking.api.security.authentication.token.TokenUserDetails;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TestTokenUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return TokenUserDetails.builder()
				.id("1")
				.authorities(List.of(Roles.ROLE_USER.getAuthority()))
				.build();
	}
}
