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
	String password,
	@NotBlank(message = "fcmToken 은 필수입니다.")
	String fcmToken
) {
	public static LoginRequest of(String email, String password, String fcmToken) {
		return LoginRequest.builder()
			.email(email)
			.password(password)
			.fcmToken(fcmToken)
			.build();
	}

}
