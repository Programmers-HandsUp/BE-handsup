package dev.handsup.notification.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotificationException extends RuntimeException {

	private final String code;

	public NotificationException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}

	public NotificationException(String message) {
		super(message);
		this.code = "NOTIF_000";
	}
}
