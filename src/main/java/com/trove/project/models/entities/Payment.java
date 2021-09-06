package com.trove.project.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trove.project.models.Auditable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Entity
public class Payment extends Auditable {

	@Id
	@Column(length = 50, unique = true)
	@NotEmpty
	@NotNull
	private String reference;

	@Column(columnDefinition = "boolean default false")
	private boolean status = false;

	@JsonIgnore
	@Min(1)
	private Long loanId;

	@JsonIgnore
	@URL
	@NotNull
	@NotEmpty
	private String authorization_url;

	public Payment() {
	}

	public Payment(@NotEmpty @NotNull String reference, @URL @NotNull @NotEmpty String authorization_url) {
		this.reference = reference;
		this.authorization_url = authorization_url;
	}

	public Payment(@NotEmpty @NotNull String reference, @URL @NotNull @NotEmpty String authorization_url,
			@NotNull @Min(1) Long loanId) {
		this.reference = reference;
		this.authorization_url = authorization_url;
		this.loanId = loanId;
	}

	@JsonIgnore
	@Transient
	public Long getCreatedById() {
		return this.getCreatedBy();
	}

}
