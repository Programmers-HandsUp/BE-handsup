package dev.handsup.auth.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auth.dto.request.AuthRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuthApiMapper {

	public static AuthRequest toAuthRequest(AuthApiRequest request) {
		return AuthRequest.of(request.email(), request.password());
	}
}
