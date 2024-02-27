package dev.handsup.auth.dto.response;

import static lombok.AccessLevel.*;

import lombok.Builder;

@Builder(access = PRIVATE)
public record TokenReIssueResponse(

	String accessToken
) {
	public static TokenReIssueResponse from(String accessToken) {
		return TokenReIssueResponse.builder()
			.accessToken(accessToken)
			.build();
	}
}
