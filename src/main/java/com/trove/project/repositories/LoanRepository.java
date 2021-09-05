package com.trove.project.repositories;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

import com.trove.project.models.entities.Loan;

public interface LoanRepository extends CrudRepository<Loan, Long> {

	@NotNull
	Slice<Loan> findAllByCreatedBy(@NotNull @Min(1L) Long userId, Pageable pageable);

	@NotNull
	List<Loan> findAllByCreatedByAndActive(@NotNull @Min(1L) Long userId, boolean active);

	boolean existsByCreatedByAndActive(@NotNull @Min(1L) Long userId, boolean active);

}
