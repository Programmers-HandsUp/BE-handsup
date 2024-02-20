package dev.handsup.user.dto.response;

public record JoinUserResponse(

	Long userId
) {
	public static JoinUserResponse from(Long userId) {
		return new JoinUserResponse(userId);
	}
}
