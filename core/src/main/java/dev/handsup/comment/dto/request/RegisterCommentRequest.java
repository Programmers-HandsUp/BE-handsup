package dev.handsup.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterCommentRequest(
	@NotBlank(message = "댓글 내용을 입력해주세요.")
	String content
){
	public static RegisterCommentRequest of(
		String content
	) {
		return new RegisterCommentRequest(content);
	}
}
