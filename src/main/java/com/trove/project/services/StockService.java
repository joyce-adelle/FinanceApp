package com.trove.project.services;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;

import com.trove.project.exceptions.ExistsException;
import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.models.entities.Stock;

@Validated
public interface StockService {

	@NotNull
	Stock createStock(@NotNull @Size(min = 4, max = 100) String name,
			@NotNull @Pattern(regexp = "[A-Z]+", message = "symbol should be in upper case and without spaces") @Size(min = 4, max = 10) String symbol,
			@NotNull @DecimalMin(value = "1.0", inclusive = true) @Digits(integer = 7, fraction = 2) BigDecimal pricePerShare,
			@NotNull @Positive Double equityValue) throws ExistsException;

	@NotNull
	Slice<Stock> getAllStocks(Pageable pageable);

	@NotNull
	Stock updatePricePerShare(@NotNull @Min(1) Long stockId,
			@NotNull @DecimalMin(value = "1.0", inclusive = true) @Digits(integer = 7, fraction = 2) BigDecimal pricePerShare);

	@NotNull
	Stock updateEquityValue(@NotNull @Min(1) Long stockId, @NotNull @Positive Double equityValue);

	@NotNull
	Stock getStock(@NotNull @Min(1) Long stockId);

	void deleteStock(@NotNull @Min(1) Long stockId) throws IllegalOperationException;

}
