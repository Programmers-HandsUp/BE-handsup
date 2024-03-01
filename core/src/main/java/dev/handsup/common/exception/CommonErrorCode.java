package dev.handsup.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

	NOT_FOUND_BY_ID("해당 Id의 엔티티가 존재하지 않습니다", "CO_001");

	private final String message;
	private final String code;
}
