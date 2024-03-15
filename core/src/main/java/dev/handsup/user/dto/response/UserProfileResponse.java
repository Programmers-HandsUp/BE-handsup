package dev.handsup.user.dto.response;

import java.util.List;

public record UserProfileResponse(

	Long userId,
	String profileImageUrl,
	String nickname,
	String dong,
	List<String> preferredProductCategories,
	int score
) {
	public static UserProfileResponse of(
		Long userId,
		String profileImageUrl,
		String nickname,
		String dong,
		List<String> preferredProductCategories,
		int score
	) {
		return new UserProfileResponse(
			userId,
			profileImageUrl,
			nickname,
			dong,
			preferredProductCategories,
			score
		);
	}
}
