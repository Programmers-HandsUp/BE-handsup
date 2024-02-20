package dev.handsup.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AuthApiRequest(
	@Email
	@NotBlank(message = "email 값이 공백입니다.")
	String email,

	@NotBlank(message = "password 값이 공백입니다.")
	String password
) {
	public static AuthApiRequest of(String email, String password) {
		return AuthApiRequest.builder()
			.email(email)
			.password(password)
			.build();
	}

}
