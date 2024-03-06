package dev.handsup.user.dto.response;

public record UserReviewLabelResponse(

	Long userReviewLabelId,
	Long reviewLabelId,
	Long userId,
	int count
) {
	public static UserReviewLabelResponse of(
		Long userReviewLabelId,
		Long reviewLabelId,
		Long userId,
		int count
	) {
		return new UserReviewLabelResponse(
			userReviewLabelId,
			reviewLabelId,
			userId,
			count
		);
	}
}
