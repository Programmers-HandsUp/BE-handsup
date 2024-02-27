package dev.handsup.auth.dto;

import static lombok.AccessLevel.*;

import org.springframework.http.HttpHeaders;

import dev.handsup.auth.dto.response.LoginDetailResponse;
import dev.handsup.auth.exception.AuthErrorCode;
import dev.handsup.auth.exception.AuthException;
import dev.handsup.common.exception.NotFoundException;
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
			throw new AuthException(AuthErrorCode.NOT_FOUND_BEARER_IN_REQUEST_ACCESS_TOKEN);
		}
		throw new NotFoundException(AuthErrorCode.NOT_FOUND_ACCESS_TOKEN_IN_REQUEST);
	}
}
