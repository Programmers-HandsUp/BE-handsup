package dev.handsup.notification.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.notification.domain.Notification;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;

	public long countNotificationsByUserEmail(String userEmail) {
		return notificationRepository.countByReceiverEmail(userEmail);
	}

	@Transactional
	public void saveNotification(
		String senderEmail,
		String receiverEmail,
		String content,
		NotificationType notificationType
	) {
		Notification notification = Notification.of(
			senderEmail, receiverEmail, content, notificationType);
		notificationRepository.save(notification);
	}
}
