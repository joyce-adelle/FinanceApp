package com.trove.project.services;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.trove.project.custom.annotations.NotEquals;
import com.trove.project.exceptions.ExistsException;
import com.trove.project.models.entities.Authority;

@Validated
public interface AuthorityService {

	@NotNull
	Iterable<Authority> getAllAuthorities();

	Authority create(
			@NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) @NotEquals(notEqualValue = "user") String authorityName)
			throws ExistsException;

	void remove(
			@NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) @NotEquals(notEqualValue = "user") String authorityName);

}
