package com.trove.project.models.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Entity
public class Portfolio {

	@JsonIgnore
	@Id
	@Column(name = "user_id")
	private Long id;

	@JsonIgnore
	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "id.user")
	@Valid
	private List<Shares> shares = new ArrayList<>();

	@Transient
	public BigDecimal getTotalValue() {
		BigDecimal sum = new BigDecimal(0.00);
		for (Shares share : this.shares) {
			sum = sum.add(share.getValue());
		}
		this.user.getWallet().add(sum);
		return sum;
	}

	public Portfolio() {

	}

}
