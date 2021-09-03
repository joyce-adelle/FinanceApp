package com.trove.project.models;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAwareUserDetails implements UserDetails {

	private static final long serialVersionUID = 318255253792512582L;
	
	private final JwtUser user;
	private final List<GrantedAuthority> grantedAuthorities;

	public UserAwareUserDetails(JwtUser user) {
		this(user, new ArrayList<GrantedAuthority>());
	}

	public UserAwareUserDetails(JwtUser user, List<GrantedAuthority> grantedAuthorities) {
		this.user = user;
		this.grantedAuthorities = grantedAuthorities;
	}

	@Override
	public List<GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getUserId() {
		return user.getId();
	}

	public JwtUser getUser() {
		return user;
	}

}
