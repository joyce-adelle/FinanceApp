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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Entity
public class Portfolio {

	@Id
	@Column(name = "user_id")
	private Long id;

	@JsonIgnore
	@OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

	@OneToMany(mappedBy = "pk.user")
	@Valid
	private List<Shares> shares = new ArrayList<>();

	@Column(columnDefinition = "Decimal(12,2) default '0.00'")
	@NotNull
	@DecimalMin(value = "0.0", inclusive = true)
	@Digits(integer = 12, fraction = 2)
	private BigDecimal wallet = new BigDecimal(0.00);

	@Column(columnDefinition = "Decimal(12,2) default '0.00'")
	@NotNull
	@DecimalMin(value = "0.0", inclusive = true)
	@Digits(integer = 12, fraction = 2)
	private BigDecimal outstandingLoan = new BigDecimal(0.00);

	@Transient
	public BigDecimal getTotalValue() {
		BigDecimal sum = new BigDecimal(0.00);
		for (Shares share : this.shares) {
			sum = sum.add(share.getValue());
		}
		this.wallet.add(sum);
		return sum;
	}

}
