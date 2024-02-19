package dev.handsup.user.dto.request;

import lombok.Builder;

@Builder
public record JoinUserRequest(

	String email,
	String password,
	String nickname,
	String si,
	String gu,
	String dong,
	String profileImageUrl
) {
}
