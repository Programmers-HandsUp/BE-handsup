package dev.handsup.auth.dto.request;

import static lombok.AccessLevel.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(access = PRIVATE)
public record LoginRequest(

	@Email
	@NotBlank(message = "email 은 필수입니다.")
	String email,

	@NotBlank(message = "password 은 필수입니다.")
	String password
) {
	public static LoginRequest of(String email, String password) {
		return LoginRequest.builder()
			.email(email)
			.password(password)
			.build();
	}

}
