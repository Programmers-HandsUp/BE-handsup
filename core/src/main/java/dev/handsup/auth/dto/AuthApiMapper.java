package dev.handsup.auth.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auth.dto.request.LoginRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuthApiMapper {

	public static LoginRequest toAuthRequest(LoginRequest request) {
		return LoginRequest.of(request.email(), request.password());
	}
}
