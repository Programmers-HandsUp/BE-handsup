package dev.handsup.auth.dto.request;

import static lombok.AccessLevel.*;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(access = PRIVATE)
public record TokenReIssueRequest(

	@NotBlank(message = "refreshToken 값이 공백입니다.")
	String refreshToken
) {
	public static TokenReIssueRequest of(String refreshToken) {
		return TokenReIssueRequest.builder()
			.refreshToken(refreshToken)
			.build();
	}
}
