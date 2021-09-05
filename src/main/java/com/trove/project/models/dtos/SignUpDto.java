package com.trove.project.models.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.trove.project.custom.annotations.ValidPassword;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class SignUpDto {

	@NotNull
	@Size(min = 4, max = 50)
	private String firstname;

	@NotNull
	@Size(min = 4, max = 50)
	private String lastname;
	
	@NotNull
	@Size(min = 4, max = 50)
	private String username;

	@NotNull
	@Email
	@Size(min = 1, max = 50)
	private String email;

	@NotNull
	@ValidPassword
	private String password;

}
