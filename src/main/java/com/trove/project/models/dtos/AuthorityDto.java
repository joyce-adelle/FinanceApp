package com.trove.project.models.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.trove.project.custom.annotations.NotEquals;

public class AuthorityDto {

	@NotNull
	@NotEmpty
	@NotEquals(notEqualValue = "user")
	private String authorityName;

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

}
