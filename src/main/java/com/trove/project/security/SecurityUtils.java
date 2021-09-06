package com.trove.project.security;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.trove.project.models.JwtUser;
import com.trove.project.models.UserAwareUserDetails;

public class SecurityUtils {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);

	private SecurityUtils() {
	}

	/**
	 * Get the username of the current user.
	 *
	 * @return the username of the current user.
	 */
	public static Optional<String> getCurrentUsername() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			LOG.debug("no authentication in security context found");
			return Optional.empty();
		}

		String username = null;
		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
			username = springSecurityUser.getUsername();
		} else if (authentication.getPrincipal() instanceof String) {
			username = (String) authentication.getPrincipal();
		}

		LOG.debug("found username '{}' in security context", username);

		return Optional.ofNullable(username);
	}

	/**
	 * Get the id of the current user.
	 *
	 * @return the id of the current user.
	 */
	public static Optional<Long> getCurrentUserId() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			LOG.debug("no authentication in security context found");
			return Optional.empty();
		}

		if (authentication.getPrincipal() instanceof UserDetails) {
			UserAwareUserDetails springSecurityUser = (UserAwareUserDetails) authentication.getPrincipal();
			return Optional.ofNullable(springSecurityUser.getUserId());
		}

		return Optional.empty();
	}

	/**
	 * Get the current user.
	 *
	 * @return the current user.
	 */
	public static Optional<JwtUser> getCurrentUser() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			LOG.debug("no authentication in security context found");
			return Optional.empty();
		}

		if (authentication.getPrincipal() instanceof UserDetails) {
			UserAwareUserDetails springSecurityUser = (UserAwareUserDetails) authentication.getPrincipal();
			return Optional.ofNullable(springSecurityUser.getUser());
		}

		return Optional.empty();
	}

	/**
	 * Get the current user role.
	 *
	 * @return the current user role.
	 */
	public static Optional<List<GrantedAuthority>> getCurrentUserRole() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			LOG.debug("no authentication in security context found");
			return Optional.empty();
		}

		if (authentication.getPrincipal() instanceof UserDetails) {
			UserAwareUserDetails springSecurityUser = (UserAwareUserDetails) authentication.getPrincipal();
			return Optional.ofNullable(springSecurityUser.getAuthorities());
		}

		return Optional.empty();
	}
}