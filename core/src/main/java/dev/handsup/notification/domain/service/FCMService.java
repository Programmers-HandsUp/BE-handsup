package dev.handsup.notification.domain.service;

import static dev.handsup.notification.domain.NotificationType.*;

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
public class FCMService implements NotificationService {

	private final FCMTokenRepository fcmTokenRepository;
	private final FirebaseMessaging firebaseMessaging;

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

	private String getFcmToken(String subscriberEmail) {
		String fcmToken = fcmTokenRepository.getFcmToken(subscriberEmail);

		if (fcmToken == null || fcmToken.isEmpty()) {
			throw new NotificationException(NotificationErrorCode.INVALID_NOTIFICATION_TARGET);
		}
		return fcmToken;
	}

	@Override
	public void sendChatMessage(String subscriberEmail, String publisherNickname) {
		sendMessage(subscriberEmail, CHAT, publisherNickname);
	}

	@Override
	public void sendCommentMessage(String subscriberEmail, String publisherNickname) {
		sendMessage(subscriberEmail, COMMENT, publisherNickname);
	}

	@Override
	public void sendBookmarkMessage(String subscriberEmail, String publisherNickname) {
		sendMessage(subscriberEmail, BOOKMARK, publisherNickname);
	}

	@Override
	public void sendPurchaseWinningMessage(String subscriberEmail) {
		sendMessage(subscriberEmail, PURCHASE_WINNING, "");
	}

	@Override
	public void sendCanceledPurchaseWinningMessage(String subscriberEmail) {
		sendMessage(subscriberEmail, CANCELED_PURCHASE_WINNING, "");
	}

	private void sendMessage(String subscriberEmail, NotificationType notificationType, String publisherNickname) {
		if (fcmTokenRepository.doNothasKey(subscriberEmail)) {
			throw new NotificationException(NotificationErrorCode.INVALID_NOTIFICATION_TARGET);
		}

		String token = getFcmToken(subscriberEmail);
		Message message = Message.builder()
			.putData("title", notificationType.getTitle())
			.putData("content", publisherNickname + notificationType.getContent())
			.setToken(token)
			.build();

		send(message);
	}

	public void saveFcmToken(LoginRequest loginRequest) {
		fcmTokenRepository.saveFcmToken(loginRequest);
	}

	public void deleteFcmToken(String subscriberEmail) {
		fcmTokenRepository.deleteFcmToken(subscriberEmail);
	}
}
