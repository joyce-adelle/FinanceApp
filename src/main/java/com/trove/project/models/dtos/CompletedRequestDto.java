package com.trove.project.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompletedRequestDto {

	private boolean done;

	public CompletedRequestDto(boolean done) {
		this.done = done;
	}

	@JsonProperty("done")
	boolean getDone() {
		return done;
	}

	void setDone(boolean done) {
		this.done = done;
	}

}
