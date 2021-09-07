package com.trove.project.controllers;

import java.math.BigDecimal;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trove.project.models.dtos.SharesDto;
import com.trove.project.models.entities.Portfolio;
import com.trove.project.services.PortfolioService;
import com.trove.project.services.SharesService;

/*
 * Controller to handle user's portfolio 
 * and to allow user buy and sell shares
 */
@RestController
@RequestMapping("/api/users/user/portfolio")
public class PortfolioController {

	@Autowired
	private PortfolioService portfolioService;

	@Autowired
	private SharesService sharesService;

	@GetMapping(value = { "", "/" })
	public ResponseEntity<Portfolio> getPortfolio() {
		return ResponseEntity.ok(this.portfolioService.getPortfolio());
	}

	@GetMapping("/value")
	public ResponseEntity<PortfolioValue> getPortfolioValue() {
		return ResponseEntity.ok(new PortfolioValue(this.portfolioService.getTotalPortfolioValue()));
	}

	@PutMapping("/buy-shares")
	public ResponseEntity<Portfolio> buyShares(@Valid @RequestBody SharesDto sharesDto) {

		return ResponseEntity.ok(this.sharesService.buyShares(sharesDto.getStockId(), sharesDto.getQuantity()));
	}

	@PutMapping("/sell-shares")
	public ResponseEntity<Portfolio> sellShares(@Valid @RequestBody SharesDto sharesDto) {

		return ResponseEntity.ok(this.sharesService.sellShares(sharesDto.getStockId(), sharesDto.getQuantity()));
	}

	static class PortfolioValue {

		private BigDecimal totalValue;

		PortfolioValue(BigDecimal totalValue) {
			this.totalValue = totalValue;
		}

		@JsonProperty("total_value")
		String getIdToken() {
			return this.totalValue.toString();
		}

		void setIdToken(BigDecimal totalValue) {
			this.totalValue = totalValue;
		}
	}

}
