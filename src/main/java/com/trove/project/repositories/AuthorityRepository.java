package com.trove.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trove.project.models.entities.Authority;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, String> {
	
}
