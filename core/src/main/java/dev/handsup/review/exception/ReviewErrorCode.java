package dev.handsup.review.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
	NOT_FOUND_REVIEW_LABEL("해당 아이디의 리뷰 라벨이 존재하지 않습니다.", "RL_000");
	private final String message;
	private final String code;
}
