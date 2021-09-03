package com.trove.project.models.entities;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Entity
public class Stock {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(length = 100)
	@NotNull
	@Size(min = 4, max = 100)
	private String name;
	
	@Column(length = 10, unique = true)
	@NotNull
	@Size(min = 4, max = 10)
	private String symbol;
	
	@Column(columnDefinition = "Decimal(7,2) default '1.00'")
	@NotNull
	@DecimalMin(value = "1.0", inclusive = true)
	@Digits(integer = 7, fraction = 2)
	private BigDecimal pricePerShare = new BigDecimal(1.00);
	
	@NotNull
	private double equityValue;

}
