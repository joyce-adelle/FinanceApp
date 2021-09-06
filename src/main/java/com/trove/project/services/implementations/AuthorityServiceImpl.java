package com.trove.project.services.implementations;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trove.project.custom.annotations.NotEquals;
import com.trove.project.exceptions.ExistsException;
import com.trove.project.exceptions.ResourceNotFoundException;
import com.trove.project.models.entities.Authority;
import com.trove.project.repositories.AuthorityRepository;
import com.trove.project.services.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Override
	public @NotNull Iterable<Authority> getAllAuthorities() {

		return this.authorityRepository.findAll();
	}

	@Override
	public Authority create(
			@NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) @NotEquals(notEqualValue = "user") String authorityName)
			throws ExistsException {

		if (this.authorityRepository.existsById(authorityName))
			throw new ExistsException("authority : " + authorityName + " already exists");

		return this.authorityRepository.save(new Authority(authorityName));
	}

	@Override
	public void remove(
			@NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) @NotEquals(notEqualValue = "user") String authorityName) {

		Authority authority = authorityRepository.findById(authorityName)
				.orElseThrow(() -> new ResourceNotFoundException("Authority not found"));

		this.authorityRepository.delete(authority);

	}

}
