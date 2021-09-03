package com.trove.project.models;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class JwtUser {

	@NotNull
	@Size(min = 1)
	private Long id;

	@NotNull
	@Size(min = 4, max = 50)
	private String firstname;

	@NotNull
	@Size(min = 4, max = 50)
	private String lastname;

	@Email
	@NotNull
	private String email;

	private boolean verified;

	@NotNull
	@Size(min = 8, max = 100)
	private String password;

	public JwtUser() {
		super();
	}

	public JwtUser(@NotNull @Size(min = 1) Long id, @Size(min = 4, max = 50) String firstname,
			@Size(min = 4, max = 50) String lastname, @Email String email, boolean verified,
			@Size(min = 8, max = 100) String password) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.verified = verified;
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		JwtUser user = (JwtUser) o;
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
