package com.trove.project.services.implementations;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
			@NotNull @Size(min = 4, max = 10) String symbol,
			@NotNull @DecimalMin(value = "1.0", inclusive = true) @Digits(integer = 7, fraction = 2) BigDecimal pricePerShare,
			@NotNull @Positive Double equityValue) {

		return this.stockRepository.save(new Stock(name, symbol, pricePerShare, equityValue));
	}

	@Override
	public @NotNull Slice<Stock> getAllStocks(Pageable pageable) {

		return this.stockRepository.findAll(pageable);
	}

	@Override
	public @NotNull Stock updateStock(@NotNull @Valid Stock stock) {

		return this.stockRepository.save(stock);
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

}
