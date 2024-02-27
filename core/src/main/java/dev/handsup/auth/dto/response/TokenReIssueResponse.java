package dev.handsup.auth.dto.response;

import static lombok.AccessLevel.*;

import lombok.Builder;

@Builder(access = PRIVATE)
public record TokenReIssueResponse(

	String accessToken
) {
	public static TokenReIssueResponse from(String accessToken) {
		String bearerAccessToken = "Bearer " + accessToken;

		return TokenReIssueResponse.builder()
			.accessToken(bearerAccessToken)
			.build();
	}
}
