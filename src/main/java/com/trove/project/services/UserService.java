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

	/**
	 * Get all users.
	 *
	 * @return all users.
	 */
	@NotNull
	Slice<User> getAllusers(Pageable pageable);

	/**
	 * Get current user and roles.
	 *
	 * @return current use and roles.
	 */
	User getCurrentUserWithAuthorities();

	/**
	 * authorize user using username and password.
	 *
	 * @return jwt token to be used for authorization.
	 */
	String login(@NotNull @Size(min = 4, max = 50) String username, @NotNull @ValidPassword String password,
			Boolean rememberMe);

	/**
	 * Creates new user.
	 * 
	 * @return jwt token to be used for authorization.
	 */
	String create(@NotNull @Size(min = 4, max = 50) String firstname, @NotNull @Size(min = 4, max = 50) String lastname,
			@NotNull @Size(min = 4, max = 50) String username, @NotNull @Email @Size(min = 1, max = 50) String email,
			@NotNull @ValidPassword String password) throws ExistsException, TransactionException;

	/**
	 * request to change logged in user password.
	 *
	 * sends mail to user with url to change password.
	 */
	void resetPasswordInit() throws TransactionException;

	/**
	 * request to change user password.
	 *
	 * sends mail to user with url to change password and a new one-time-password.
	 */
	void forgotPasswordInit(@NotNull String username) throws TransactionException;

	/**
	 * change user password.
	 * 
	 */
	void resetPasswordFinish(@NotNull @NotEmpty String token, @NotNull @ValidPassword String oldPassword,
			@NotNull @ValidPassword String newPassword) throws TransactionException, IllegalOperationException;

	/**
	 * request to change logged in user email.
	 *
	 * sends mail to new email with url to verify.
	 */
	void changeEmailInit(@Email @NotNull String newEmail) throws ExistsException, TransactionException;

	/**
	 * request to verify user email.
	 *
	 * sends mail to user email with url to verify.
	 */
	void verifyInit() throws TransactionException, IllegalOperationException;

	/**
	 * email verification of user.
	 */
	void verifyFinish(@NotNull @NotEmpty String token) throws TransactionException, IllegalOperationException;

	/**
	 * add any authority to specific user.
	 *
	 * @return the updated user.
	 */
	User addAuthority(@NotNull @Min(1L) Long userId,
			@NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) String authorityName);

	/**
	 * remove any authority except "user" from specific user.
	 *
	 * @return the updated user.
	 */
	User removeAuthority(@NotNull @Min(1L) Long userId,
			@NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) @NotEquals(notEqualValue = "user") String authorityName);

	/**
	 * Get specific user with id.
	 *
	 * @return the user with id.
	 */
	User getUserWithAuthorities(@NotNull @Min(1) Long userId);

	/**
	 * update current user details.
	 *
	 * @return the updated user.
	 */
	User update(@Valid User user);

}
