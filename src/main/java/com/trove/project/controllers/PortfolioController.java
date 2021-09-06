package com.trove.project.controllers;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trove.project.models.dtos.SharesDto;
import com.trove.project.models.entities.Portfolio;
import com.trove.project.services.PortfolioService;
import com.trove.project.services.SharesService;

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
	public ResponseEntity<BigDecimal> getPortfolioValue() {
		return ResponseEntity.ok(this.portfolioService.getTotalPortfolioValue());
	}

	@PutMapping("/buy-shares")
	public ResponseEntity<Portfolio> buyShares(@PathVariable @NotNull @Min(value = 1L, message = "Invalid id.") Long id,
			@Valid @RequestBody SharesDto sharesDto) {

		return ResponseEntity.ok(this.sharesService.buyShares(sharesDto.getStockId(), sharesDto.getQuantity()));
	}

	@PutMapping("/sell-shares")
	public ResponseEntity<Portfolio> sellShares(
			@PathVariable @NotNull @Min(value = 1L, message = "Invalid id.") Long id,
			@Valid @RequestBody SharesDto sharesDto) {

		return ResponseEntity.ok(this.sharesService.sellShares(sharesDto.getStockId(), sharesDto.getQuantity()));
	}

}
