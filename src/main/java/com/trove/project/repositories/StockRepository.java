package com.trove.project.repositories;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trove.project.models.entities.Stock;

@Repository
public interface StockRepository extends CrudRepository<Stock, Long> {

	@NotNull
	Slice<Stock> findAll(Pageable pageable);

	boolean existsBySymbol(@NotNull @Pattern(regexp = "[A-Z]+") @Size(min = 4, max = 10) String symbol);

}
