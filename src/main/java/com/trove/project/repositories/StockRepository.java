package com.trove.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trove.project.models.entities.Stock;

@Repository
public interface StockRepository extends CrudRepository<Stock, Long> {

}
