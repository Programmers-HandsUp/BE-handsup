package dev.handsup.comment.dto.request;

public record RegisterCommentRequest(
	String content
){
	public static RegisterCommentRequest of(
		String content
	) {
		return new RegisterCommentRequest(content);
	}
}
