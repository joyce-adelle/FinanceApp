package com.trove.project.models.dtos;

import javax.validation.constraints.NotNull;
import com.trove.project.custom.annotations.ValidPassword;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ResetPasswordDto extends TokenDto {

	@NotNull
	@ValidPassword
	private String oldPassword;

	@NotNull
	@ValidPassword
	private String newPassword;

}
