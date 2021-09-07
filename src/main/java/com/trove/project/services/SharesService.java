package com.trove.project.services;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

import com.trove.project.models.entities.Portfolio;

@Validated
public interface SharesService {

	/**
	 * current user buy shares of a stock.
	 *
	 * @return user portfolio with newly bought shares.
	 */
	@NotNull
	Portfolio buyShares(@NotNull @Min(1) Long stockId, @Positive @NotNull Double quantity);

	/**
	 * current user sell shares of a stock.
	 *
	 * @return user portfolio with update of shares.
	 */
	@NotNull
	Portfolio sellShares(@NotNull @Min(1) Long stockId, @Positive @NotNull Double quantity);

}
