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

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {

	@JsonProperty
    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

	@JsonProperty
    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

	@JsonIgnore
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

	@JsonProperty
    @LastModifiedDate
    @Column(name = "last_modified_at")
    private Date lastModifiedAt;
}
