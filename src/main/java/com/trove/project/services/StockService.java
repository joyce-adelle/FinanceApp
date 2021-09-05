package com.trove.project.services;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;

import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.models.entities.Stock;

@Validated
public interface StockService {

	@NotNull
	Stock createStock(@NotNull @Size(min = 4, max = 100) String name, @NotNull @Size(min = 4, max = 10) String symbol,
			@NotNull @DecimalMin(value = "1.0", inclusive = true) @Digits(integer = 7, fraction = 2) BigDecimal pricePerShare,
			@NotNull @Positive Double equityValue);

	@NotNull
	Slice<Stock> getAllStocks(Pageable pageable);

	@NotNull
	Stock updateStock(@NotNull @Valid Stock stock);

	@NotNull
	Stock getStock(@NotNull @Min(1) Long stockId);

	void deleteStock(@NotNull @Min(1) Long stockId) throws IllegalOperationException;

}
