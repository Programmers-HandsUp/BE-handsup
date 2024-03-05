package dev.handsup.auth.dto;

import static lombok.AccessLevel.*;

import org.springframework.http.HttpHeaders;

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

	public static Cookie toCookie(LoginDetailResponse response) {
		Cookie cookie = new Cookie("refreshToken", response.refreshToken());
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(14 * 24 * 60 * 60); // 14일

		return cookie;
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
