package com.trove.project.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.trove.project.custom.annotations.ValidPassword;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for storing a user's credentials.
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class LoginDto {

	@NotNull
	@Size(min = 4, max = 50)
	private String username;

	@NotNull
	@ValidPassword
	private String password;

	private Boolean rememberMe;

	@Override
	public String toString() {
		return "LoginDto{" + "username='" + username + '\'' + ", rememberMe=" + rememberMe + '}';
	}
}
