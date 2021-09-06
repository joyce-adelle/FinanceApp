package com.trove.project.services.implementations;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trove.project.exceptions.ExistsException;
import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.ResourceNotFoundException;
import com.trove.project.models.entities.Stock;
import com.trove.project.repositories.SharesRepository;
import com.trove.project.repositories.StockRepository;
import com.trove.project.services.StockService;

@Service
@Transactional
public class StockServiceImpl implements StockService {

	@Autowired
	StockRepository stockRepository;

	@Autowired
	SharesRepository sharesRepository;

	@Override
	public @NotNull Stock createStock(@NotNull @Size(min = 4, max = 100) String name,
			@NotNull @Pattern(regexp = "[A-Z]+", message = "symbol should be in upper case and without spaces") @Size(min = 4, max = 10) String symbol,
			@NotNull @DecimalMin(value = "1.0", inclusive = true) @Digits(integer = 7, fraction = 2) BigDecimal pricePerShare,
			@NotNull @Positive Double equityValue) throws ExistsException {

		if (this.stockRepository.existsBySymbol(symbol))
			throw new ExistsException("There is a stock with symbol: " + symbol);

		return this.stockRepository.save(new Stock(name, symbol.toUpperCase(), pricePerShare, equityValue));
	}

	@Override
	public @NotNull Slice<Stock> getAllStocks(Pageable pageable) {

		return this.stockRepository.findAll(pageable);
	}

	@Override
	public void deleteStock(@NotNull @Min(1) Long stockId) throws IllegalOperationException {

		Stock stock = this.getStock(stockId);
		if (this.sharesRepository.existsByIdStock(stock))
			throw new IllegalOperationException("This stock has shares and cannot be deleted");

		this.stockRepository.delete(stock);
	}

	@Override
	public @NotNull Stock getStock(@NotNull @Min(1) Long stockId) {

		return this.stockRepository.findById(stockId)
				.orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
	}

	@Override
	public @NotNull Stock updatePricePerShare(@NotNull @Min(1) Long stockId,
			@NotNull @DecimalMin(value = "1.0", inclusive = true) @Digits(integer = 7, fraction = 2) BigDecimal pricePerShare) {

		Stock stock = this.getStock(stockId);
		stock.setPricePerShare(pricePerShare);

		return this.stockRepository.save(stock);
	}

	@Override
	public @NotNull Stock updateEquityValue(@NotNull @Min(1) Long stockId, @NotNull @Positive Double equityValue) {

		Stock stock = this.getStock(stockId);
		stock.setEquityValue(equityValue);

		return this.stockRepository.save(stock);
	}

}
