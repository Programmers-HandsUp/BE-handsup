package dev.handsup.auth.dto;

import static lombok.AccessLevel.*;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import dev.handsup.auth.dto.response.LoginDetailResponse;
import dev.handsup.auth.exception.AuthErrorCode;
import dev.handsup.common.exception.NotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuthMapper {

	public static LoginDetailResponse of(String refreshToken, String accessToken) {
		return LoginDetailResponse.builder()
			.refreshToken(refreshToken)
			.accessToken(accessToken)
			.build();
	}

	public static String toAccessToken(HttpServletRequest request) {
		String bearerAccessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (bearerAccessToken != null) {
			if (bearerAccessToken.startsWith("Bearer ")) {
				return bearerAccessToken.substring(7);
			}
			throw new NotFoundException(AuthErrorCode.NOT_FOUND_BEARER_IN_REQUEST_ACCESS_TOKEN);
		}
		throw new NotFoundException(AuthErrorCode.NOT_FOUND_ACCESS_TOKEN_IN_REQUEST);
	}

	public static ResponseCookie toCookie(LoginDetailResponse response) {
		return ResponseCookie.from("refreshToken", response.refreshToken())
			.path("/")
			.sameSite("None")
			.httpOnly(true)
			.secure(false)
			.maxAge(Duration.ofDays(15))
			.build();
	}

	public static String extractRefreshTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("refreshToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		throw new NotFoundException(AuthErrorCode.NOT_FOUND_REFRESH_TOKEN_IN_COOKIES);
	}
}
