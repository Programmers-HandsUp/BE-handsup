package dev.handsup.user.dto.response;

import java.util.List;

public record UserBasicInfoResponse(

	String profileImageUrl,
	String nickname,
	String dong,
	List<String> preferredProductCategories,
	int score
) {
	public static UserBasicInfoResponse of(
		String profileImageUrl,
		String nickname,
		String dong,
		List<String> preferredProductCategories,
		int score
	) {
		return new UserBasicInfoResponse(
			profileImageUrl,
			nickname,
			dong,
			preferredProductCategories,
			score
		);
	}
}
