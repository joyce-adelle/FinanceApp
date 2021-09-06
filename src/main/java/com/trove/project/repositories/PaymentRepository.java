package com.trove.project.repositories;

import java.util.Optional;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trove.project.models.entities.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {

	Optional<Payment> findOneByReferenceAndStatus(@NotNull String reference, boolean status);

	boolean existsByReference(@NotNull String reference);

	Optional<Payment> findOneByCreatedByAndLoanIdAndStatus(@NotNull @Min(1L) Long userId, @NotNull @Min(1) Long loanId,
			boolean status);

	@NotNull
	Slice<Payment> findAllByCreatedBy(@NotNull @Min(1L) Long userId, Pageable pageable);

}
