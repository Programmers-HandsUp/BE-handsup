package dev.handsup.user.dto.response;

public record UserReviewResponse(

	Long reviewId,
	String writerNickName,
	String writerProfileImageUrl,
	String content
) {
	public static UserReviewResponse of(
		Long reviewId,
		String writerNickName,
		String writerProfileImageUrl,
		String content
	) {
		return new UserReviewResponse(
			reviewId,
			writerNickName,
			writerProfileImageUrl,
			content
		);
	}
}
