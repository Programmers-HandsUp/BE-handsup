package dev.handsup.notification.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements ErrorCode {

	FAILED_TO_SEND_FIREBASE_MESSAGE("FIREBASE 의 메시지 전송에 실패했습니다.", "NOTIF_001"),
	FAILED_TO_SEND_MESSAGE("메시지 전송에 실패했습니다.", "NOTIF_002"),
	INVALID_NOTIFICATION_TARGET("유효하지 않은 알림 대상입니다.", "NOTIF_003");

	private final String message;
	private final String code;
}
