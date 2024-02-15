package dev.handsup.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserJoinRequest(

	@NotBlank
	@Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]){0,19}"
		+ "@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z]){0,19}+[.][a-zA-Z]{2,3}$",
		message = "이메일 주소 양식을 확인해주세요")
	String email,

	@NotBlank
	String password,

	@NotBlank
	String nickname,

	@NotBlank
	String si,

	@NotBlank
	String gu,

	@NotBlank
	String dong,

	@NotBlank
	String profileImageUrl
) {
}
