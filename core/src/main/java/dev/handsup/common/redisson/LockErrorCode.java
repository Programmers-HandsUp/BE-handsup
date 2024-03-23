package dev.handsup.common.redisson;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LockErrorCode implements ErrorCode {

	FAILED_TO_GET_LOCK("주어진 시간 동안 락을 획득하는데 실패했습니다.", "rl_001");

	private final String message;
	private final String code;
}
