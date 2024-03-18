package dev.handsup.auth.service;

import static dev.handsup.auth.exception.AuthErrorCode.*;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.handsup.common.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	private static final String USER_ID = "userId";
	private final int tokenValidSeconds;
	private final Key key;

	public JwtProvider(
		@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.token-validity-in-seconds}") int tokenValidSeconds
	) {
		this.tokenValidSeconds = tokenValidSeconds;

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public Long getClaim(String token) {
		Claims claimsBody = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return Long.valueOf((Integer)claimsBody.get(USER_ID));
	}

	public String createAccessToken(Long userId) {
		Date now = new Date();

		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim(USER_ID, userId)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidSeconds * 1000L))    // 30분
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String createRefreshToken(Long userId) {
		Date now = new Date();

		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim(USER_ID, userId)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidSeconds * 1000L * 2 * 24 * 14))    // 14일
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public void validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			throw new ValidationException(TOKEN_EXPIRED);
		} catch (io.jsonwebtoken.UnsupportedJwtException e) {
			throw new ValidationException(UNSUPPORTED_TOKEN);
		} catch (io.jsonwebtoken.MalformedJwtException e) {
			throw new ValidationException(MALFORMED_TOKEN);
		} catch (Exception e) {
			throw new ValidationException(INVALID_TOKEN_ETC);
		}
	}

}
