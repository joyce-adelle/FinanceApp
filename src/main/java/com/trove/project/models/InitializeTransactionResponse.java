package com.trove.project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Response returned from the paystack initialize transaction api
 */

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitializeTransactionResponse {
	private boolean status;
	private String message;
	private Data data;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class Data {
		/**
		 * this is the redirect url that the user would use to make the payment
		 */
		private String authorization_url;
		/**
		 * this code identifies the payment url
		 */
		private String access_code;
		/**
		 * the unique reference used to identify this transaction
		 */
		private String reference;

	}

}
