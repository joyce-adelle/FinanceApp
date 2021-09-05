package com.trove.project.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.trove.project.security.JwtAccessDeniedHandler;
import com.trove.project.security.JwtAuthenticationEntryPoint;
import com.trove.project.security.JwtFilter;

@Configuration
@EnableCaching
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CorsFilter corsFilter;

	@Autowired
	private JwtAuthenticationEntryPoint authenticationErrorHandler;

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Configure paths and requests that should be ignored by Spring Security
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")

				// allow anonymous resource requests
				.antMatchers("/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js", "/h2-console/**");
	}

	// Configure security settings
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// don't need CSRF because our token is invulnerable
				.csrf().disable()

				.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

				.exceptionHandling().authenticationEntryPoint(authenticationErrorHandler)
				.accessDeniedHandler(jwtAccessDeniedHandler)

				// enable h2-console
				.and().headers().frameOptions().sameOrigin()

				// create no session
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and().authorizeRequests().antMatchers("/api/login").permitAll().antMatchers("/api/register")
				.permitAll().antMatchers("/api/verify/init").permitAll().antMatchers("/api/verify/finish").permitAll()
				.antMatchers("/api/reset-password/init").permitAll().antMatchers("/api/reset-password/finish")
				.permitAll().antMatchers("/api/forgot-password").permitAll().antMatchers("/api/payments/verify/topup")
				.permitAll().antMatchers("/api/payments/webhook/verify").permitAll()

				.antMatchers("/api/users").hasAuthority("user")
				// .antMatchers("/api/hiddenmessage").hasAuthority("admin")

				.anyRequest().authenticated();

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
