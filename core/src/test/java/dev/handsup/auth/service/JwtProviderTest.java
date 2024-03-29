package dev.handsup.auth.service;

import static dev.handsup.auth.exception.AuthErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.security.Key;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.handsup.common.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@DisplayName("[JwtProvider 테스트]")
@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

	private final Long userId = 123L;
	private JwtProvider jwtProvider;
	private Key key;
	private int tokenValidSeconds;
	private String secretKey;

	@BeforeEach
	void setUp() {
		secretKey = "fdflsjhflkwejfblkjhvuixochvuhsofiuesafbidsfab223411";
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		tokenValidSeconds = 3600;
		jwtProvider = new JwtProvider(secretKey, tokenValidSeconds);
	}

	@Test
	@DisplayName("[Access Token을 성공적으로 만든다]")
	void createAccessTokenTest() {
		// when
		String token = jwtProvider.createAccessToken(userId);

		Jws<Claims> claimsJws = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
		Claims claims = claimsJws.getBody();

		// then
		assertThat(token).isNotNull();
		assertThat(userId).isEqualTo(claims.get("userId", Long.class));
		assertThat(claims.getIssuedAt()).isNotNull();
		assertThat(claims.getExpiration()).isNotNull();
		assertThat(claims.getExpiration().getTime() - claims.getIssuedAt().getTime())
			.isCloseTo(tokenValidSeconds * 1000L, within(1000L));    // 토큰 생성 자체에 드는 시간 고려
	}

	@Test
	@DisplayName("[Refresh Token을 성공적으로 만든다]")
	void createRefreshTokenTest() {
		// when
		String token = jwtProvider.createRefreshToken(userId);

		Jws<Claims> claimsJws = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
		Claims claims = claimsJws.getBody();

		// then
		assertThat(token).isNotNull();
		assertThat(userId).isEqualTo(claims.get("userId", Long.class));
		assertThat(claims.getIssuedAt()).isNotNull();
		assertThat(claims.getExpiration()).isNotNull();
		assertThat(claims.getExpiration().getTime() - claims.getIssuedAt().getTime())
			.isCloseTo(tokenValidSeconds * 1000L * 2 * 24 * 14, within(1000L));    // 토큰 생성 자체에 드는 시간 고려
	}

	@Test
	@DisplayName("[Claim 에서 UserId를 뽑아온다]")
	void getClaimTest() {
		// given
		String token = jwtProvider.createAccessToken(userId);

		// when
		Long extractedUserId = jwtProvider.getClaim(token);

		// then
		assertThat(extractedUserId).isEqualTo(userId);
	}

	@Test
	@DisplayName("[유효성 검사에서 정상적인 토큰은 성공한다]")
	void validateValidTokenTest() {
		// given
		String validToken = jwtProvider.createAccessToken(userId);

		// when, then
		assertDoesNotThrow(() -> jwtProvider.validateToken(validToken));
	}

	@Test
	@DisplayName("[유효성 검사에서 구조가 안 맞는 토큰은 실패한다]")
	void validateInValidTokenTest() {
		// given
		String invalidToken = "invalidToken";

		// when, then
		assertThatThrownBy(
			() -> jwtProvider.validateToken(invalidToken)
		)
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(MALFORMED_TOKEN.getMessage());
	}

	@Test
	@DisplayName("[유효성 검사에서 유효 기간이 지난 토큰은 실패한다]")
	void validateExpiredTokenTest() {
		// given
		jwtProvider = new JwtProvider(secretKey, -1000000);
		String expiredToken = jwtProvider.createAccessToken(userId);

		// when, then
		assertThatThrownBy(
			() -> jwtProvider.validateToken(expiredToken)
		)
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(TOKEN_EXPIRED.getMessage());
	}

	@Test
	@DisplayName("[유효성 검사에서 잘못된 서명의 토큰은 실패한다]")
	void validateTokenWithAlteredSignatureTest() {
		// given
		String validToken = jwtProvider.createAccessToken(userId);
		String alteredToken = validToken.substring(0, validToken.length() - 4) + "abcd";

		// when, then
		assertThatThrownBy(
			() -> jwtProvider.validateToken(alteredToken)
		)
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(INVALID_TOKEN_ETC.getMessage());
	}
}
