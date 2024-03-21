package dev.handsup.user.dto.response;

public record UserReviewLabelResponse(

	String reviewLabelValue,
	int count
) {
	public static UserReviewLabelResponse of(
		String reviewLabelValue,
		int count
	) {
		return new UserReviewLabelResponse(
			reviewLabelValue,
			count
		);
	}
}
