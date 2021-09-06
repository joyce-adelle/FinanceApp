package com.trove.project.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InitializePaymentWithPaystackDto {

	private String authorization_url;

	public InitializePaymentWithPaystackDto(String authorization_url) {
		this.authorization_url = authorization_url;
	}

	@JsonProperty("authorization_url")
	public String getAuthorization_url() {
		return authorization_url;
	}

	public void setAuthorization_url(String authorization_url) {
		this.authorization_url = authorization_url;
	}

}
