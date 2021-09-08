package com.trove.project.services.implementations;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trove.project.exceptions.ResourceNotFoundException;
import com.trove.project.models.entities.Portfolio;
import com.trove.project.repositories.PortfolioRepository;
import com.trove.project.security.SecurityUtils;
import com.trove.project.services.PortfolioService;

@Service
public class PortfolioServiceImpl implements PortfolioService {

	@Autowired
	PortfolioRepository portfolioRepository;

	@Override
	public @NotNull Portfolio getPortfolio() {

		return SecurityUtils.getCurrentUserId().flatMap(portfolioRepository::findById)
				.orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));
	}

	@Override
	public @NotNull BigDecimal getTotalPortfolioValue() {

		return this.getPortfolio().getTotalValue();
	}

}
