package dev.handsup.notification.dto.response;

public record CountUserNotificationsResponse(

	long count
) {
	public static CountUserNotificationsResponse from(long count) {
		return new CountUserNotificationsResponse(count);
	}
}
