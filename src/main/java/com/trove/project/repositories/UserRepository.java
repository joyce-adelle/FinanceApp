package com.trove.project.repositories;

import java.util.Optional;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.trove.project.models.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByUsername(@NotNull @Size(min = 4, max = 50) String username);

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesById(@NotNull @Min(1L) Long id);

	Optional<User> findOneByEmail(@Email @NotNull String email);

	Optional<User> findOneByUsername(@NotNull @Size(min = 4, max = 50) String username);

	boolean existsByEmail(@Email @NotNull String email);

	boolean existsByUsername(@NotNull @Size(min = 4, max = 50) String username);

	@NotNull
	Slice<User> findAll(Pageable pageable);

}
