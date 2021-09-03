package com.trove.project.models.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trove.project.models.Auditable;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
@Entity
public class User extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
	@SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", allocationSize = 1)
	private Long id;

	@Column(length = 50)
	@NotNull
	@Size(min = 4, max = 50)
	private String firstname;

	@Column(length = 50)
	@NotNull
	@Size(min = 4, max = 50)
	private String lastname;

	@NotNull
	@Email
	@Size(min = 1, max = 50)
	@Column(unique = true)
	private String email;

	@Column(columnDefinition = "boolean default false")
	private boolean verified = false;

	@JsonIgnore
	@NotNull
	@Size(min = 8, max = 100)
	private String password;

	@JsonIgnore
	private Date verifiedAt;

	@ManyToMany
	@JoinTable(name = "user_authority", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "authority_name", referencedColumnName = "name") })
	@BatchSize(size = 20)
	private Set<Authority> authorities = new HashSet<>();
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @NotNull
    private Portfolio portfolio;

	public User() {
		this.portfolio = new Portfolio();
	}

	public User(@NotNull @Size(min = 4, max = 50) String firstname, @NotNull @Size(min = 4, max = 50) String lastname,
			@NotNull @Email @Size(min = 1, max = 50) String email, boolean verified,
			@NotNull @Size(min = 8, max = 100) String password, Set<Authority> authorities) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.verified = verified;
		this.password = password;
		this.authorities = authorities;
		this.portfolio = new Portfolio();
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
		this.verifiedAt = new Date();
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User) o;
		return id.equals(user.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", verified=" + verified + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", email=" + email + '}';
	}

}
