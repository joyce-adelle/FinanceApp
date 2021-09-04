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
import com.trove.project.custom.annotations.ValidPassword;
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
	@Size(min = 4, max = 50)
	@Column(unique = true, length = 50)
	private String username;

	@NotNull
	@Email
	@Column(unique = true)
	private String email;

	@Column(columnDefinition = "boolean default false")
	private boolean verified = false;

	@JsonIgnore
	@NotNull
	@ValidPassword
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

	}

	public User(@NotNull @Size(min = 4, max = 50) String firstname, @NotNull @Size(min = 4, max = 50) String lastname,
			@NotNull @Size(min = 4, max = 50) String username, @NotNull @Email String email,
			@NotNull @Size(min = 8, max = 100) String password, Set<Authority> authorities) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
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

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
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
				+ ", username=" + username + ", email=" + email + '}';
	}

}
