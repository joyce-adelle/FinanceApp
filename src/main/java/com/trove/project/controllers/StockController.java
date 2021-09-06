package com.trove.project.controllers;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trove.project.exceptions.ExistsException;
import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.TransactionException;
import com.trove.project.models.dtos.CompletedRequestDto;
import com.trove.project.models.dtos.StockDto;
import com.trove.project.models.dtos.UpdateEquityValueDto;
import com.trove.project.models.dtos.UpdatePricePerShareDto;
import com.trove.project.models.entities.Stock;
import com.trove.project.services.StockService;

@RestController
@RequestMapping("/api/stock")
public class StockController {

	@Autowired
	private StockService stockService;

	@GetMapping(value = { "", "/", "/page" })
	public ResponseEntity<Slice<Stock>> getAllStocks(Pageable pageable) {
		return ResponseEntity.ok(this.stockService.getAllStocks(pageable));
	}

	@PostMapping("/create")
	ResponseEntity<Stock> createStock(@RequestBody @Valid @NotNull(message = "The user cannot be null.") StockDto stock)
			throws ExistsException, TransactionException {

		return ResponseEntity.ok(this.stockService.createStock(stock.getName(), stock.getSymbol().toUpperCase().strip(),
				stock.getPricePerShare(), stock.getEquityValue()));
	}

	@PutMapping("/stock/{id}/price")
	public ResponseEntity<Stock> updatePricePerShare(
			@PathVariable @NotNull @Min(value = 1L, message = "Invalid id.") Long id,
			@Valid @RequestBody UpdatePricePerShareDto pricePerShare) {

		return ResponseEntity.ok(this.stockService.updatePricePerShare(id, pricePerShare.getPricePerShare()));
	}

	@PutMapping("/stock/{id}/equity-value")
	public ResponseEntity<Stock> updateEquityValue(
			@PathVariable @NotNull @Min(value = 1L, message = "Invalid id.") Long id,
			@Valid @RequestBody UpdateEquityValueDto equityValue) {

		return ResponseEntity.ok(this.stockService.updateEquityValue(id, equityValue.getEquityValue()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CompletedRequestDto> deleteStock(
			@PathVariable @NotNull @Min(value = 1L, message = "Invalid id.") Long id) throws IllegalOperationException {
		this.stockService.deleteStock(id);
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Stock> getStock(@PathVariable @NotNull @Min(value = 1L, message = "Invalid id.") Long id) {
		return ResponseEntity.ok(this.stockService.getStock(id));
	}

}
