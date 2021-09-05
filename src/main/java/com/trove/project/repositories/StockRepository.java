package com.trove.project.repositories;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trove.project.models.entities.Stock;

@Repository
public interface StockRepository extends CrudRepository<Stock, Long> {

	@NotNull
	Slice<Stock> findAll(Pageable pageable);

}
