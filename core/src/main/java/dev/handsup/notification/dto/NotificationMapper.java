package dev.handsup.notification.dto;

import static lombok.AccessLevel.*;

import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.dto.response.NotificationResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class NotificationMapper {

	public static NotificationResponse toNotificationResponse(
		Long notificationId,
		NotificationType notificationType,
		String content,
		String senderProfileImageUrl,
		Long auctionId,
		String auctionImageUrl
	) {
		return NotificationResponse.of(
			notificationId,
			notificationType,
			content,
			senderProfileImageUrl,
			auctionId,
			auctionImageUrl
		);
	}
}
