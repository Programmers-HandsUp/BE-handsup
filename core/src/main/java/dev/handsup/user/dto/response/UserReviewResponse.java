package dev.handsup.user.dto.response;

public record UserReviewResponse(

	String writerNickName,
	String writerProfileImageUrl,
	String content
) {
	public static UserReviewResponse of(
		String writerNickName,
		String writerProfileImageUrl,
		String content
	) {
		return new UserReviewResponse(
			writerNickName,
			writerProfileImageUrl,
			content
		);
	}
}
