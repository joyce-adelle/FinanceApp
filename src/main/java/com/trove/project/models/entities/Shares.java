package com.trove.project.models.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trove.project.models.UserStockId;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/*
 * User shares in each stock and quantity
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Entity
public class Shares {

	@EmbeddedId
	@JsonIgnore
	private UserStockId id;

	@Positive
	@NotNull
	private Double quantity;

	public Shares() {
		super();
	}

	public Shares(@Valid User user, @Valid Stock stock, @Positive @NotNull Double quantity) {
		id = new UserStockId();
		id.setUser(user);
		id.setStock(stock);
		this.quantity = quantity;
	}

	@Transient
	public Stock getStock() {
		return this.id.getStock();
	}

	@Transient
	public BigDecimal getValue() {
		return this.id.getStock().getPricePerShare().multiply(BigDecimal.valueOf(this.quantity)).setScale(2,
				RoundingMode.UP);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Shares other = (Shares) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Shares{" + "userid=" + id.getUser().getId() + ", quantity=" + quantity + ", stock="
				+ this.getStock().toString() + '}';
	}

}
