package dev.handsup.notification.service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import dev.handsup.auction.domain.Auction;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.dto.request.SaveFCMTokenRequest;
import dev.handsup.notification.repository.FCMTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FCMService {

	private final FCMTokenRepository fcmTokenRepository;
	private final FirebaseMessaging firebaseMessaging;
	private final NotificationService notificationService;

	public void sendMessage(
		String senderEmail,
		String senderNickname,
		String receiverEmail,
		NotificationType notificationType,
		Auction auction
	) {
		String fcmToken = fcmTokenRepository.getFcmToken(receiverEmail);
		if (fcmToken == null) {
			return;
		}

		if (notificationType.equals(NotificationType.CANCELED_PURCHASE_TRADING) ||
			notificationType.equals(NotificationType.PURCHASE_WINNING)) {
			senderNickname = "";
		}

		Message message = Message.builder()
			.setNotification(Notification.builder()
				.setTitle(notificationType.getTitle())
				.setBody(senderNickname + notificationType.getContent())
				.build())
			.setToken(fcmToken)
			.build();

		send(message, receiverEmail);

		notificationService.saveNotification(
			senderEmail, receiverEmail, notificationType.getContent(), notificationType, auction
		);
	}

	private void send(Message message, String receiverEmail) {
		try {
			firebaseMessaging.send(message);
			log.info("Sent message: {}, to: {}", message, receiverEmail);
		} catch (FirebaseMessagingException e) {
			throw new ValidationException(e.getMessage());
		}
	}

	public void saveFcmToken(String userEmail, SaveFCMTokenRequest request) {
		fcmTokenRepository.saveFcmToken(userEmail, request.fcmToken());
	}

	public void deleteFcmToken(String email) {
		fcmTokenRepository.deleteFcmToken(email);
	}
}
