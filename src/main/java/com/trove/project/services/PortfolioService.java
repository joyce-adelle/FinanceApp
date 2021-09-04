package com.trove.project.services;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.trove.project.models.entities.Portfolio;

@Validated
public interface PortfolioService {

	@NotNull
	Portfolio getPortfolio();

	@NotNull
	BigDecimal getTotalPortfolioValue();

}
