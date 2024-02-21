package dev.handsup.user.dto.request;

import static lombok.AccessLevel.*;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = PRIVATE)
public record JoinUserRequest(

	String email,
	String password,
	String nickname,
	String si,
	String gu,
	String dong,
	String profileImageUrl
) {
	public static JoinUserRequest of(
		String email,
		String password,
		String nickname,
		String si,
		String gu,
		String dong,
		String profileImageUrl
	) {
		return JoinUserRequest.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.si(si)
			.gu(gu)
			.dong(dong)
			.profileImageUrl(profileImageUrl)
			.build();
	}
}
