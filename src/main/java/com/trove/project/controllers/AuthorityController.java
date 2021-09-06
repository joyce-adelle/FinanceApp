package com.trove.project.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trove.project.custom.annotations.NotEquals;
import com.trove.project.exceptions.ExistsException;
import com.trove.project.models.dtos.AuthorityDto;
import com.trove.project.models.dtos.CompletedRequestDto;
import com.trove.project.models.entities.Authority;
import com.trove.project.services.AuthorityService;

@RestController
@RequestMapping("/api/authority")
public class AuthorityController {

	@Autowired
	private AuthorityService authorityService;

	@GetMapping(value = { "", "/" })
	public ResponseEntity<@NotNull Iterable<Authority>> getAllAuthorities() {

		return ResponseEntity.ok(this.authorityService.getAllAuthorities());
	}

	@PostMapping(value = { "", "/" })
	ResponseEntity<Authority> createAuthority(
			@RequestBody @Valid @NotNull(message = "The user cannot be null.") AuthorityDto authority)
			throws ExistsException {

		return ResponseEntity.ok(this.authorityService.create(authority.getAuthorityName()));
	}

	@DeleteMapping("/{authorityName}")
	public ResponseEntity<CompletedRequestDto> deleteStock(
			@PathVariable @NotNull @Pattern(regexp = "[a-z]+", message = "authority name should be in lower case and without spaces") @Size(min = 4, max = 50) @NotEquals(notEqualValue = "user") String authorityName) {
		this.authorityService.remove(authorityName);
		return ResponseEntity.ok(new CompletedRequestDto(true));
	}

}
