package dev.handsup.comment.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

	COMMENT_NOT_AVAIL_AUCTION("댓글을 달 수 없는 경매 상태입니다", "CT_001");

	private final String message;
	private final String code;
}
