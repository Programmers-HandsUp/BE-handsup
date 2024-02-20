package dev.handsup.auth.dto.request;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
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
