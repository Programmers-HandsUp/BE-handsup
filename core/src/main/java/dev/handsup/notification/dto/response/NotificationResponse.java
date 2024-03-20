package dev.handsup.notification.dto.response;

import dev.handsup.notification.domain.NotificationType;

public record NotificationResponse(

	Long notificationId,
	NotificationType notificationType,
	String content,
	String senderProfileImageUrl,
	Long auctionId,
	String auctionImageUrl
) {
	public static NotificationResponse of(
		Long notificationId,
		NotificationType notificationType,
		String content,
		String senderProfileImageUrl,
		Long auctionId,
		String auctionImageUrl
	) {
		return new NotificationResponse(
			notificationId,
			notificationType,
			content,
			senderProfileImageUrl,
			auctionId,
			auctionImageUrl
		);
	}
}
