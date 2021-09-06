package com.trove.project.services;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;

import com.trove.project.custom.annotations.NotEquals;
import com.trove.project.custom.annotations.ValidPassword;
import com.trove.project.exceptions.ExistsException;
import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.TransactionException;
import com.trove.project.models.entities.User;

@Validated
public interface UserService {

	@NotNull
	Slice<User> getAllusers(Pageable pageable);

	User getCurrentUserWithAuthorities();

	String login(@NotNull @Size(min = 4, max = 50) String username, @NotNull @ValidPassword String password,
			Boolean rememberMe);

	void create(@NotNull @Size(min = 4, max = 50) String firstname, @NotNull @Size(min = 4, max = 50) String lastname,
			@NotNull @Size(min = 4, max = 50) String username, @NotNull @Email @Size(min = 1, max = 50) String email,
			@NotNull @ValidPassword String password) throws ExistsException, TransactionException;

	void resetPasswordInit() throws TransactionException;

	void forgotPasswordInit(@NotNull String username) throws TransactionException;

	void resetPasswordFinish(@NotNull @NotEmpty String token, @NotNull @ValidPassword String oldPassword,
			@NotNull @ValidPassword String newPassword) throws TransactionException, IllegalOperationException;

	void changeEmailInit(@Email @NotNull String newEmail) throws ExistsException, TransactionException;

	void verifyInit() throws TransactionException, IllegalOperationException;

	void verifyFinish(@NotNull @NotEmpty String token) throws TransactionException, IllegalOperationException;

	User addAuthority(@NotNull @Min(1L) Long userId,
			@NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) String authorityName);

	User removeAuthority(@NotNull @Min(1L) Long userId,
			@NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) @NotEquals(notEqualValue = "user") String authorityName);

	User getUserWithAuthorities(@NotNull @Min(1) Long userId);

	User update(@Valid User user);

}
