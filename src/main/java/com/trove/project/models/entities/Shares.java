package com.trove.project.models.entities;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trove.project.models.UserStockPk;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Entity
public class Shares {

	@EmbeddedId
	@JsonIgnore
	private UserStockPk pk;

	@NotNull
	private Double quantity;

	public Shares() {
		super();
	}

	public Shares(@Valid User user, @Valid Stock stock, Double quantity) {
		pk = new UserStockPk();
		pk.setUser(user);
		pk.setStock(stock);
		this.quantity = quantity;
	}

	@Transient
	public Stock getStock() {
		return this.pk.getStock();
	}

	@Transient
	public BigDecimal getValue() {
		return this.pk.getStock().getPricePerShare().multiply(new BigDecimal(getQuantity())).setScale(2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());

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
		if (pk == null) {
			if (other.pk != null) {
				return false;
			}
		} else if (!pk.equals(other.pk)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Shares{" + "userid=" + pk.getUser().getId() + ", quantity=" + quantity + ", dataplan="
				+ this.getStock().toString() + '}';
	}

}
