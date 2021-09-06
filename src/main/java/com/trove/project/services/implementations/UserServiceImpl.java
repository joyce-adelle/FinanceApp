package com.trove.project.services.implementations;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trove.project.custom.annotations.NotEquals;
import com.trove.project.custom.annotations.ValidPassword;
import com.trove.project.exceptions.ExistsException;
import com.trove.project.exceptions.IllegalOperationException;
import com.trove.project.exceptions.ResourceNotFoundException;
import com.trove.project.exceptions.TransactionException;
import com.trove.project.models.JwtUser;
import com.trove.project.models.entities.Authority;
import com.trove.project.models.entities.Portfolio;
import com.trove.project.models.entities.User;
import com.trove.project.repositories.AuthorityRepository;
import com.trove.project.repositories.UserRepository;
import com.trove.project.security.JwtTokenUtil;
import com.trove.project.security.SecurityUtils;
import com.trove.project.services.EmailService;
import com.trove.project.services.UserService;
import com.trove.project.utilities.RandomString;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private EmailService emailService;

	@Value("${verify.url}")
	private String verifyUrl;

	@Value("${resetPassword.url}")
	private String resetPasswordUrl;

	@Value("${forgotPassword.url}")
	private String forgotPasswordUrl;

	@Override
	public String login(@NotNull @Size(min = 4, max = 50) String username, @NotNull @ValidPassword String password,
			Boolean rememberMe) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		boolean remMe = (rememberMe == null) ? false : rememberMe;

		return jwtTokenUtil.createToken(authentication, remMe);
	}

	@Override
	public User getCurrentUserWithAuthorities() {
		System.out.println(SecurityUtils.getCurrentUsername());
		return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
	public User getUserWithAuthorities(@NotNull @Min(1L) Long userId) {
		return userRepository.findOneWithAuthoritiesById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
	public void create(@NotNull @Size(min = 4, max = 50) String firstname,
			@NotNull @Size(min = 4, max = 50) String lastname, @NotNull @Size(min = 4, max = 50) String username,
			@NotNull @Email @Size(min = 1, max = 50) String email, @NotNull @ValidPassword String password)
			throws ExistsException, TransactionException {

		if (userRepository.existsByEmail(email)) {
			throw new ExistsException("There is an account with email adress: " + email);
		}

		if (userRepository.existsByUsername(username)) {
			throw new ExistsException("There is an account with username: " + email);
		}

		Set<Authority> authorities = Stream.of(authorityRepository.findById("user").get())
				.collect(Collectors.toCollection(HashSet::new));

		User user = new User(firstname, lastname, username, email, passwordEncoder.encode(password), authorities);
		Portfolio portfolio = new Portfolio();

		user.setPortfolio(portfolio);
		portfolio.setUser(user);

		userRepository.save(user);

		try {
			verifyInit(email, email, firstname, "welcome");
		} catch (MessagingException e) {
			throw new TransactionException();
		} catch (MailSendException e) {
			throw new TransactionException();
		}

	}

	@Override
	public void forgotPasswordInit(@NotNull String username) throws TransactionException {
		User user = userRepository.findOneByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		String password = RandomString.randomString(12);
		String jwt = jwtTokenUtil.createToken(username, passwordEncoder.encode(password), false);
		Map<String, Object> templateModel = Map.of("name", user.getFirstname(), "url",
				resetPasswordUrl + "?token=" + jwt, "otp", password);
		try {
			emailService.sendMessage(user.getEmail(), "Forgot Password", templateModel, "forgotPassword");
		} catch (MessagingException e) {
			throw new TransactionException();
		} catch (MailSendException e) {
			throw new TransactionException();
		}

	}

	@Override
	public void resetPasswordFinish(@NotNull @NotEmpty String token, @NotNull @ValidPassword String oldPassword,
			@NotNull @ValidPassword String newPassword) throws IllegalOperationException {
		try {

			Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);

			if (passwordEncoder.matches(oldPassword, (String) claims.get("password"))) {
				System.out.println(oldPassword);
				System.out.println(claims.get("password").toString());
				User user = userRepository.findOneByUsername(claims.getSubject())
						.orElseThrow(() -> new ResourceNotFoundException("User not found"));
				user.setPassword(passwordEncoder.encode(newPassword));
				userRepository.save(user);
			}

			else {
				throw new IllegalOperationException("incorrect password");
			}

		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			throw new IllegalOperationException("corrupted token");
		} catch (ExpiredJwtException e) {
			throw new IllegalOperationException("Link expired. Please repeat procedure");
		} catch (UnsupportedJwtException e) {
			throw new IllegalOperationException("corrupted token");
		} catch (IllegalArgumentException e) {
			throw new IllegalOperationException("corrupted token");
		} catch (ResourceNotFoundException e) {
			throw e;
		}

	}

	@Override
	public void changeEmailInit(@Email @NotNull String newEmail) throws ExistsException, TransactionException {
		if (userRepository.existsByEmail(newEmail)) {
			throw new ExistsException("There is an account with email adress:" + newEmail);
		}
		JwtUser user = SecurityUtils.getCurrentUser()
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		try {
			verifyInit(user.getEmail(), newEmail, user.getFirstname(), "changeEmail");
		} catch (MessagingException e) {
			throw new TransactionException();
		} catch (MailSendException e) {
			throw new TransactionException();
		}
	}

	@Override
	public void verifyInit() throws TransactionException, IllegalOperationException {
		JwtUser user = SecurityUtils.getCurrentUser()
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		if (user.isVerified())
			throw new IllegalOperationException("user already verified");
		try {
			verifyInit(user.getEmail(), user.getEmail(), user.getFirstname(), "welcome");
		} catch (MessagingException e) {
			throw new TransactionException();
		} catch (MailSendException e) {
			throw new TransactionException();
		}
	}

	@Override
	public void verifyFinish(@NotNull @NotEmpty String token) throws IllegalOperationException {
		try {
			Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
			String oldEmail = (String) claims.get("oldEmail");
			String newEmail = (String) claims.get("newEmail");
			User user = userRepository.findOneByEmail(oldEmail)
					.orElseThrow(() -> new ResourceNotFoundException("User not found"));
			user.setVerified(true);
			user.setEmail(newEmail);
			userRepository.save(user);

		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			throw new IllegalOperationException("corrupted token");
		} catch (ExpiredJwtException e) {
			throw new IllegalOperationException("Link expired.");
		} catch (UnsupportedJwtException e) {
			throw new IllegalOperationException("corrupted token");
		} catch (IllegalArgumentException e) {
			throw new IllegalOperationException("corrupted token");
		} catch (ResourceNotFoundException e) {
			throw e;
		}

	}

	@Override
	public User update(@Valid User user) {
		return userRepository.save(user);
	}

	@Override
	public User addAuthority(@NotNull @Min(1L) Long userId,
			@NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) String authorityName) {

		User user = this.getUserWithAuthorities(userId);
		Authority authority = this.authorityRepository.findById(authorityName)
				.orElseThrow(() -> new ResourceNotFoundException("Authority not found"));

		user.addAuthority(authority);

		this.userRepository.save(user);
		return this.getUserWithAuthorities(userId);

	}

	@Override
	public User removeAuthority(@NotNull @Min(1L) Long userId,
			@NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) @NotEquals(notEqualValue = "user") String authorityName) {

		User user = this.getUserWithAuthorities(userId);
		Authority authority = this.authorityRepository.findById(authorityName)
				.orElseThrow(() -> new ResourceNotFoundException("Authority not found"));

		user.removeAuthority(authority);

		this.userRepository.save(user);

		return this.getUserWithAuthorities(userId);
	}

	private void verifyInit(String oldEmail, String newEmail, String name, String template) throws MessagingException {
		String jwt = jwtTokenUtil.createToken(oldEmail, newEmail);
		Map<String, Object> templateModel = Map.of("name", name, "url", verifyUrl + "?token=" + jwt);
		emailService.sendMessage(newEmail, "Verify Email", templateModel, template);
	}

	@Override
	public void resetPasswordInit() throws TransactionException {
		JwtUser user = SecurityUtils.getCurrentUser()
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		String jwt = jwtTokenUtil.createToken(user.getUsername(), user.getPassword(), true);
		Map<String, Object> templateModel = Map.of("name", user.getFirstname(), "url",
				resetPasswordUrl + "?token=" + jwt);
		try {
			emailService.sendMessage(user.getEmail(), "Reset Password", templateModel, "resetPassword");
		} catch (MessagingException e) {
			throw new TransactionException();
		} catch (MailSendException e) {
			throw new TransactionException();
		}
	}

	@Override
	public @NotNull Slice<User> getAllusers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

}
