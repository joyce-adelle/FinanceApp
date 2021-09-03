package com.trove.project.security;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.trove.project.models.JwtUser;
import com.trove.project.models.UserAwareUserDetails;
import com.trove.project.models.entities.User;
import com.trove.project.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
		if (!new EmailValidator().isValid(login, null))
			throw new UsernameNotFoundException("invalid email: " + login);

		return userRepository.findOneWithAuthoritiesByEmail(login).map(user -> createSpringSecurityUser(login, user))
				.orElseThrow(() -> new UsernameNotFoundException(
						"User with email " + login + " was not found in the database"));
	}

	private UserAwareUserDetails createSpringSecurityUser(String lowercaseLogin, User user) {
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
		JwtUser jwtUser = new JwtUser(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(),
				user.isVerified(), user.getPassword());
		return new UserAwareUserDetails(jwtUser, grantedAuthorities);
	}
}
