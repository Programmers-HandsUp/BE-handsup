package dev.handsup.notification.domain.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotificationException extends RuntimeException {

	private final String code;

	public NotificationException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}

	public NotificationException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.code = errorCode.getCode();
	}
}
