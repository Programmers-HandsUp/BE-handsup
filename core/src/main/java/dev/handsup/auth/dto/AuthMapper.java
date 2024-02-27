package dev.handsup.auth.dto;

import static lombok.AccessLevel.*;

import org.springframework.http.ResponseCookie;

import dev.handsup.auth.dto.response.LoginResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuthMapper {

	public static String createCookie(String name, String value, long maxAge) {
		return ResponseCookie.from(name, value)
			.httpOnly(true)
			.secure(true) // HTTPS에서만 전송
			// 같은 사이트 내에서만 쿠키를 전송하고, GET 요청과 같은 안전한 요청에서만 타 사이트 요청에 의해 쿠키를 전송
			.sameSite("Lax")
			.path("/")
			.maxAge(maxAge)
			.build()
			.toString();
	}

	public static LoginResponse of(String refreshToken, String accessToken) {
		return LoginResponse.builder()
			.refreshToken(refreshToken)
			.accessToken(accessToken)
			.build();
	}
}
