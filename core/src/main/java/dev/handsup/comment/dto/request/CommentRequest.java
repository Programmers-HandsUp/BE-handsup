package dev.handsup.comment.dto.request;

public record CommentRequest (
	String content
){
	public static CommentRequest of(
		String content
	) {
		return new CommentRequest(content);
	}
}
