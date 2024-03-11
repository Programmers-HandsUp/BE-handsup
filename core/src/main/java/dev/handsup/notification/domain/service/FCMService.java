package dev.handsup.notification.domain.service;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import dev.handsup.auth.dto.request.LoginRequest;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.domain.exception.NotificationErrorCode;
import dev.handsup.notification.domain.exception.NotificationException;
import dev.handsup.notification.domain.repository.FCMTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FCMService {

	private final FCMTokenRepository fcmTokenRepository;
	private final FirebaseMessaging firebaseMessaging;
	private final NotificationService notificationService;

	private void send(Message message) {
		try {
			ApiFuture<String> stringApiFuture = firebaseMessaging.sendAsync(message);
			String apiFutureResponse = stringApiFuture.get();
			log.info("Sent message: " + apiFutureResponse);
		} catch (InterruptedException e) {
			// 현재 스레드가 대기 중 인터럽트 됐을 때의 처리
			Thread.currentThread().interrupt();    //  현재 스레드의 인터럽트 상태를 복원
			throw new NotificationException(NotificationErrorCode.FAILED_TO_SEND_MESSAGE_INTERRUPT, e.getCause());
		} catch (ExecutionException e) {
			// sendAsync() 실행 중 예외 발생 처리
			Throwable cause = e.getCause();
			if (cause instanceof FirebaseMessagingException) {
				throw new NotificationException(NotificationErrorCode.FAILED_TO_SEND_FIREBASE_MESSAGE, cause);
			} else {
				throw new NotificationException(NotificationErrorCode.FAILED_TO_SEND_MESSAGE, cause);
			}
		}
	}

	private String getFcmToken(String receiverEmail) {
		String fcmToken = fcmTokenRepository.getFcmToken(receiverEmail);

		if (fcmToken == null || fcmToken.isEmpty()) {
			throw new NotificationException(NotificationErrorCode.INVALID_NOTIFICATION_TARGET);
		}
		return fcmToken;
	}

	public void sendMessage(
		String senderEmail,
		String senderNickname,
		String receiverEmail,
		NotificationType notificationType
	) {
		if (fcmTokenRepository.doNotHasKey(receiverEmail)) {
			throw new NotificationException(NotificationErrorCode.INVALID_NOTIFICATION_TARGET);
		}

		if (notificationType.equals(NotificationType.CANCELED_PURCHASE_WINNING) ||
			notificationType.equals(NotificationType.PURCHASE_WINNING)) {
			senderNickname = "";
		}

		String fcmToken = getFcmToken(receiverEmail);
		Message message = Message.builder()
			.putData("title", notificationType.getTitle())
			.putData("content", senderNickname + notificationType.getContent())
			.setToken(fcmToken)
			.build();

		send(message);

		notificationService.saveNotification(
			senderEmail, receiverEmail, notificationType.getContent(), notificationType
		);
	}

	public void saveFcmToken(LoginRequest loginRequest) {
		fcmTokenRepository.saveFcmToken(loginRequest);
	}

	public void deleteFcmToken(String email) {
		fcmTokenRepository.deleteFcmToken(email);
	}
}
