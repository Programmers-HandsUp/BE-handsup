package dev.handsup.auction.dto.response;

public record UserInfo(
	Long userId,
	String nickname,
	String profileImageUrl,
	String dong,
	int score
) {
	public static UserInfo of(
		Long userId,
		String nickname,
		String profileImageUrl,
		String dong,
		int score
	) {
		return new UserInfo(
			userId,
			nickname,
			profileImageUrl,
			dong,
			score
		);
	}
}
