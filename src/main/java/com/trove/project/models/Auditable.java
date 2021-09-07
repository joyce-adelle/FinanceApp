package com.trove.project.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {

	@JsonIgnore
	@CreatedBy
	private Long createdBy;

	@JsonProperty
	@CreatedDate
	private Date createdAt;

	@JsonIgnore
	@LastModifiedBy
	private Long lastModifiedBy;

	@JsonProperty
	@LastModifiedDate
	private Date lastModifiedAt;
}
