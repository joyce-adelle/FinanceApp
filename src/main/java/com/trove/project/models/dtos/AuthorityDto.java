package com.trove.project.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.trove.project.custom.annotations.NotEquals;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class AuthorityDto {

	@NotNull
	@Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces")
	@Size(min = 4, max = 50)
	@NotEquals(notEqualValue = "user")
	private String authorityName;

}
