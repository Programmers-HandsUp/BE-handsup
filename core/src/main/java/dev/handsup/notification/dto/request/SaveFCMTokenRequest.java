package dev.handsup.notification.dto.request;

import static lombok.AccessLevel.*;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(access = PRIVATE)
public record SaveFCMTokenRequest(

	@NotNull(message = "fcmToken 은 null일 수 없습니다.")
	String fcmToken
) {
	public static SaveFCMTokenRequest from(String fcmToken) {
		return SaveFCMTokenRequest.builder()
			.fcmToken(fcmToken)
			.build();
	}
}
