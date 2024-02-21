package dev.handsup.auth.dto.request;

import static lombok.AccessLevel.*;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = PRIVATE)
public record AuthRequest(

	String email,
	String password
) {
	public static AuthRequest of(String email, String password) {
		return AuthRequest.builder()
			.email(email)
			.password(password)
			.build();
	}

}
