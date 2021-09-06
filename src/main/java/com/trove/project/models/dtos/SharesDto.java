package com.trove.project.models.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class SharesDto {

	@NotNull
	@Min(1)
	private Long stockId;

	@Positive
	@NotNull
	private Double quantity;

}
