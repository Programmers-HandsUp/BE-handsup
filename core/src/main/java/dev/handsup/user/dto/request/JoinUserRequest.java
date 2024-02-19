package dev.handsup.user.dto.request;

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
