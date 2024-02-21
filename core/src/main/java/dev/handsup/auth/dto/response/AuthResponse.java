package dev.handsup.auth.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
	String refreshToken,
	String accessToken
) {
	public static AuthResponse of(String refreshToken, String accessToken) {
		return AuthResponse.builder()
			.refreshToken(refreshToken)
			.accessToken(accessToken)
			.build();
	}
}
