package dev.handsup.comment.dto.response;

public record CommentResponse(
	Long writerId,
	String nickname,
	String profileImageUrl,
	String content,
	boolean isSeller,
	String createdAt
) {
	public static CommentResponse of(
		Long writerId,
		String nickname,
		String profileImageUrl,
		String content,
		boolean isSeller,
		String createdAt
	) {
		return new CommentResponse(writerId, nickname, profileImageUrl, content, isSeller, createdAt);
	}
}
