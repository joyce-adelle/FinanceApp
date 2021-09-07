package com.trove.project.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.trove.project.security.SecurityUtils;

import java.util.Optional;

/*
 * configures user id or the default admin id
 * for auditing
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

	@Bean
	public AuditorAware<Long> auditorProvider() {

		return () -> Optional
				.ofNullable(SecurityUtils.getCurrentUserId().isPresent() ? SecurityUtils.getCurrentUserId().get() : 2L);
	}
}
