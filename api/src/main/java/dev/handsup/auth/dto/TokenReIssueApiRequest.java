package dev.handsup.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenReIssueApiRequest(
	@NotBlank(message = "refreshToken 값이 공백입니다.")
	String refreshToken
) {
}
