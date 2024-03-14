package dev.handsup.notification.dto.response;

import dev.handsup.notification.domain.NotificationType;

public record NotificationResponse(

	NotificationType notificationType,
	String content,
	String senderProfileImageUrl,
	Long auctionId,
	String auctionImageUrl
) {
	public static NotificationResponse of(
		NotificationType notificationType,
		String content,
		String senderProfileImageUrl,
		Long auctionId,
		String auctionImageUrl
	) {
		return new NotificationResponse(
			notificationType, content, senderProfileImageUrl, auctionId, auctionImageUrl);
	}
}
