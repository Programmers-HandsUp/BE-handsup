package dev.handsup.comment.dto.response;

public record CommentResponse(
	Long writerId,
	String nickname,
	String profileImageUrl,
	String content,
	boolean isSeller
) {
	public static CommentResponse of(
		Long writerId,
		String nickname,
		String profileImageUrl,
		String content,
		boolean isSeller
	) {
		return new CommentResponse(writerId, nickname, profileImageUrl, content, isSeller);
	}
}
