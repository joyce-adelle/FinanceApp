package com.trove.project.repositories;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trove.project.models.UserStockId;
import com.trove.project.models.entities.Shares;
import com.trove.project.models.entities.Stock;
import com.trove.project.models.entities.User;

@Repository
public interface SharesRepository extends CrudRepository<Shares, UserStockId> {

	Optional<Shares> findOneByIdUserAndIdStock(@NotNull @Valid User user, @NotNull @Valid Stock stock);

	boolean existsByIdStock(@NotNull @Valid Stock stock);

}
