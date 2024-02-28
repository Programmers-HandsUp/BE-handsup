package dev.handsup.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginDetailResponse(

	String refreshToken,
	String accessToken
) {
	public static LoginDetailResponse of(String refreshToken, String accessToken) {
		return LoginDetailResponse.builder()
			.refreshToken(refreshToken)
			.accessToken(accessToken)
			.build();
	}
}
