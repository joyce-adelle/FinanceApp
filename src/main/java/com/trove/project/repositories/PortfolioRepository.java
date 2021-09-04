package com.trove.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trove.project.models.entities.Portfolio;

@Repository
public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {

}
