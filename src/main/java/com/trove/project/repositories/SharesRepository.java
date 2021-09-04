package com.trove.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trove.project.models.entities.Shares;

@Repository
public interface SharesRepository extends CrudRepository<Shares, Long> {

}
