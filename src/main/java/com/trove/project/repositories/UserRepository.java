package com.trove.project.repositories;

import java.util.Optional;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trove.project.models.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByEmail(String email);

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesById(@NotNull @Min(1L) Long id);

	Optional<User> findOneByEmail(@NotNull String email);

	boolean existsByEmail(@NotNull String email);

	@Modifying
	@Query(value = "INSERT INTO user_authority (user_id, authority_name) VALUES (:userId, :authorityName)", nativeQuery = true)
	@Transactional
	void addAuthority(@Param("userId") @NotNull @Min(1L) Long userId,
			@Param("authorityName") @NotNull @NotEmpty String authorityName);

	@Modifying
	@Query(value = "delete from user_authority where user_id = :userId and authority_name = :authorityName", nativeQuery = true)
	@Transactional
	void removeAuthority(@Param("userId") @NotNull @Min(1L) Long userId,
			@Param("authorityName") @NotNull @NotEmpty String authorityName);

	@NotNull
	Slice<User> findAll(Pageable pageable);
}
