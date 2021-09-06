package com.trove.project.models;

import java.math.BigDecimal;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyTransactionResponse {

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class Data {
		/**
		 * the amount for a transaction
		 */
		private BigDecimal amount;
		/**
		 * the currency for the transaction
		 */
		private String currency;
		/**
		 * the date the transaction occured
		 */
		private String transaction_date;
		/**
		 * status of transaction if the transaction is successful, status = "success"
		 */
		private String status;
		/**
		 * the unique reference that identifies the transaction
		 */
		private String reference;
		/**
		 * the type of paystack account the transaction was made, could be "test" or
		 * "live"
		 */
		private String domain;
		
		 private HashMap<String, Object> metadata;
		/**
		 * details about the transaction or why it failed
		 */
		private String gateway_response;
		/**
		 * message for invalid request
		 */
		private String message;
		/**
		 * the channel the transaction was made, could be "bank" or "card"
		 */
		private String channel;
		/**
		 * the ip adress of the user performing the transaction
		 */
		private String ip_address;
		/**
		 *
		 */
		private String fees;

		/**
		 * the date the transaction was paid
		 */
		private String paid_at;

	}

	/**
	 * this status is "true" if the request is successful and false if is not NOTE:
	 * This does not mean the transaction was successful, data.status holds that
	 * information
	 */
	private String status;
	/**
	 * information about the request, could be "verification successful" or "invalid
	 * key"
	 */
	private String message;
	/**
	 * contains details about the transaction
	 */
	private Data data;

	public VerifyTransactionResponse() {
	}

}
