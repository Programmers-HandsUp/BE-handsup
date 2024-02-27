package dev.handsup.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
	String refreshToken,
	String accessToken
) {
	public static LoginResponse of(String refreshToken, String accessToken) {
		String bearerAccessToken = "Bearer " + accessToken;
		String bearerRefreshToken = "Bearer " + refreshToken;

		return LoginResponse.builder()
			.refreshToken(bearerRefreshToken)
			.accessToken(bearerAccessToken)
			.build();
	}
}
