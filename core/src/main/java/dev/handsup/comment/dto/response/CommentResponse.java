package dev.handsup.comment.dto.response;

public record CommentResponse (
	Long userId,
	String nickname,
	String profileImageUrl,
	String content,
	boolean isSeller
){
}
