package dev.handsup.user.dto;

import static lombok.AccessLevel.*;

import dev.handsup.user.dto.request.JoinUserRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ApiUserMapper {
	public static JoinUserRequest toJoinUserRequest(JoinUserApiRequest request) {
		return JoinUserRequest.builder()
			.email(request.email())
			.password(request.password())
			.nickname(request.password())
			.si(request.si())
			.gu(request.gu())
			.dong(request.dong())
			.profileImageUrl(request.profileImageUrl())
			.build();
	}
}
