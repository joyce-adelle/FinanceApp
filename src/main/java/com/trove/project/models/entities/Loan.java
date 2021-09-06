package com.trove.project.models.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.trove.project.models.Auditable;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
@Entity
public class Loan extends Auditable {

	@Id
	@Positive
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Positive
	private Double monthlyInterestRate;

	@NotNull
	@Min(6)
	@Max(12)
	private Integer numberOfMonths;

	@Column(columnDefinition = "Decimal(7,2) default '1.00'")
	@NotNull
	@DecimalMin(value = "1.0", inclusive = true)
	@Digits(integer = 7, fraction = 2)
	private BigDecimal loanAmount;

	@Column(columnDefinition = "Decimal(7,2) default '0.00'")
	@NotNull
	@DecimalMin(value = "0.0", inclusive = true)
	@Digits(integer = 7, fraction = 2)
	private BigDecimal amountPaid = new BigDecimal(0.00);

	@Column(columnDefinition = "boolean default true")
	private boolean active = true;

	public Loan() {

	}

	public Loan(@NotNull @Positive Double monthlyInterestRate, @NotNull @Min(6) @Max(12) Integer numberOfMonths,
			@NotNull @DecimalMin(value = "1.0", inclusive = true) @Digits(integer = 7, fraction = 2) BigDecimal loanAmount) {

		this.monthlyInterestRate = monthlyInterestRate;
		this.numberOfMonths = numberOfMonths;
		this.loanAmount = loanAmount;
	}

	@Transient
	public BigDecimal getMonthlyPayment() {

		return this.loanAmount
				.multiply(new BigDecimal(
						(this.monthlyInterestRate * Math.pow(1 + this.monthlyInterestRate, this.numberOfMonths))
								/ (Math.pow(1 + this.monthlyInterestRate, this.numberOfMonths) - 1)))
				.setScale(2, RoundingMode.UP);

	}

	@Transient
	public BigDecimal getTotalPayment() {
		return getMonthlyPayment().multiply(new BigDecimal(this.numberOfMonths)).setScale(2, RoundingMode.UP);
	}

	@Transient
	public BigDecimal getBalance() {
		return this.getTotalPayment().subtract(this.amountPaid).setScale(2);
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
