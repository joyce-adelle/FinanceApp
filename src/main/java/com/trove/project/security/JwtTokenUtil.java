package com.trove.project.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trove.project.models.JwtUser;
import com.trove.project.models.UserAwareUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil implements InitializingBean {

	private final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
	private static final String AUTHORITIES_KEY = "auth";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.token-validity-in-seconds}")
	private long tokenValidityInSeconds;

	@Value("${jwt.token-validity-in-seconds-for-remember-me}")
	private long tokenValidityInSecondsForRememberMe;

	@Value("${jwt.token-validity-in-seconds-for-verify}")
	private long tokenValidityInSecondsForVerify;

	private Key key;

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	}

	public String createToken(Authentication authentication, boolean rememberMe) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity;
		if (rememberMe) {
			validity = new Date(now + this.tokenValidityInSecondsForRememberMe);
		} else {
			validity = new Date(now + this.tokenValidityInSeconds);
		}

		return Jwts.builder().setSubject(authentication.getName())
				.claim("user", ((UserAwareUserDetails) authentication.getPrincipal()).getUser())
				.claim(AUTHORITIES_KEY, authorities).signWith(key, SignatureAlgorithm.HS512).setExpiration(validity)
				.compact();
	}

	public String createToken(String username, String password, boolean pass) {
		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInSecondsForVerify);
		return Jwts.builder().setSubject(username).claim("password", password).signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity).compact();
	}

	public String createToken(String oldEmail, String newEmail) {
		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInSecondsForVerify);

		return Jwts.builder().claim("oldEmail", oldEmail).claim("newEmail", newEmail)
				.signWith(key, SignatureAlgorithm.HS512).setExpiration(validity).compact();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = this.getAllClaimsFromToken(token);
		ObjectMapper mapper = new ObjectMapper();

		List<GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		UserAwareUserDetails principal = new UserAwareUserDetails(
				mapper.convertValue(claims.get("user"), JwtUser.class), authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT signature.");
			log.trace("Invalid JWT signature trace: {}", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token.");
			log.trace("Expired JWT token trace: {}", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token.");
			log.trace("Unsupported JWT token trace: {}", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT token compact of handler are invalid.");
			log.trace("JWT token compact of handler are invalid trace: {}", e);
		}
		return false;
	}

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);

	}
}
