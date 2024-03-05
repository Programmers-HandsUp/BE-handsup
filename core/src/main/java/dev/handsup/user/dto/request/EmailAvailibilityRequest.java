package dev.handsup.user.dto.request;

import static lombok.AccessLevel.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder(access = PRIVATE)
public record EmailAvailibilityRequest(

	@NotBlank(message = "email 은 필수입니다.")
	@Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]){0,19}"
		+ "@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]){0,19}[.][a-zA-Z]{2,3}$",
		message = "이메일 주소 양식을 확인해주세요.")
	String email
) {
	public static EmailAvailibilityRequest from(String email) {
		return EmailAvailibilityRequest.builder()
			.email(email)
			.build();
	}
}
