package dev.handsup.user.dto.response;

import dev.handsup.user.domain.Address;
import dev.handsup.user.domain.User;

public record UserDetailResponse(
	Long userId,
	String email,
	String password,
	String nickname,
	int score,
	Address address,
	String profileImageUrl,
	int reportCount,
	long readNotificationCount
) {
	public static UserDetailResponse of(User user) {
		return new UserDetailResponse(
			user.getId(),
			user.getEmail(),
			user.getPassword(),
			user.getNickname(),
			user.getScore(),
			user.getAddress(),
			user.getProfileImageUrl(),
			user.getReportCount(),
			user.getReadNotificationCount()
		);
	}
}
