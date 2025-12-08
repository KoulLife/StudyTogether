package com.koul.StudyTogether.global.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

	private static final String AUTHORITIES_KEY = "auth";

	// application.yml 등에 jwt.secret 설정이 필요합니다. 임시로 하드코딩된 값을 사용하지 않도록 주의하세요.
	// 여기서는 편의상 기본값을 넣어두거나, 실제로는 환경변수/프로퍼티에서 가져와야 합니다.
	@Value("${jwt.secret:c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWrwaXNlLXNlY3JldC1rZXkK}")
	private String secret;

	@Value("${jwt.access-token-validity-in-seconds:1800}") // 30분
	private long accessTokenValidityInMilliseconds;

	@Value("${jwt.refresh-token-validity-in-seconds:604800}") // 7일
	private long refreshTokenValidityInMilliseconds;

	private Key key;

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.accessTokenValidityInMilliseconds *= 1000;
		this.refreshTokenValidityInMilliseconds *= 1000;
	}

	public String createAccessToken(Authentication authentication) {
		return createToken(authentication, accessTokenValidityInMilliseconds);
	}

	public String createRefreshToken(Authentication authentication) {
		return createToken(authentication, refreshTokenValidityInMilliseconds);
	}

	private String createToken(Authentication authentication, long validity) {
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validityDate = new Date(now + validity);

		return Jwts.builder()
				.subject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
				.signWith(key)
				.expiration(validityDate)
				.compact();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser()
				.verifyWith((javax.crypto.SecretKey) key)
				.build()
				.parseSignedClaims(token)
				.getPayload();

		Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		UserDetails principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}
}
