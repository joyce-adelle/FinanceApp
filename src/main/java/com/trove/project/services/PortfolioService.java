package com.trove.project.services;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.trove.project.models.entities.Portfolio;

@Validated
public interface PortfolioService {

	/**
	 * get current user portfolio.
	 *
	 * @return user portfolio.
	 */
	@NotNull
	Portfolio getPortfolio();

	/**
	 * get current user portfolio value.
	 *
	 * @return user portfolio value.
	 */
	@NotNull
	BigDecimal getTotalPortfolioValue();

}
