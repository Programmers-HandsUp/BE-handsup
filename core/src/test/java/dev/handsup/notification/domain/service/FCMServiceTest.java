package dev.handsup.notification.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;

import dev.handsup.auction.domain.Auction;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.exception.NotificationErrorCode;
import dev.handsup.notification.exception.NotificationException;
import dev.handsup.notification.repository.FCMTokenRepository;
import dev.handsup.notification.service.FCMService;
import dev.handsup.notification.service.NotificationService;
import dev.handsup.user.domain.User;

@DisplayName("[FCM 알림 서비스 테스트]")
@ExtendWith(MockitoExtension.class)
class FCMServiceTest {

	private final User receiver = UserFixture.user();
	private final Auction auction = AuctionFixture.auction();
	@Mock
	private FCMTokenRepository fcmTokenRepository;
	@Mock
	private FirebaseMessaging firebaseMessaging;
	@Mock
	private NotificationService notificationService;
	@InjectMocks
	private FCMService fcmService;

	@Test
	@DisplayName("메시지를 성공적으로 보낸다]")
	void sendMessageSuccessTest() throws FirebaseMessagingException {
		// given
		String receiverEmail = receiver.getEmail();
		String fcmToken = "fcmToken123";

		given(fcmTokenRepository.hasKey(receiverEmail)).willReturn(true);
		given(fcmTokenRepository.getFcmToken(receiverEmail)).willReturn(fcmToken);

		// when
		fcmService.sendMessage(
			"senderEmail",
			"senderNickname",
			receiverEmail,
			NotificationType.BOOKMARK,
			auction
		);

		// then
		verify(firebaseMessaging, times(1)).send(any());
	}

	@Test
	@DisplayName("[메시지를 보내는데 실패한다 - 메시지 구독자의 FCM 토큰이 없을 때]")
	void sendMessageFailTest() {
		// given
		String receiverEmail = receiver.getEmail();
		given(fcmTokenRepository.hasKey(receiverEmail)).willReturn(true);

		// when, then
		assertThatThrownBy(() ->
			fcmService.sendMessage(
				"senderEmail",
				"senderNickname",
				receiverEmail,
				NotificationType.BOOKMARK,
				auction
			))
			.isInstanceOf(NotificationException.class)
			.hasMessageContaining(NotificationErrorCode.INVALID_NOTIFICATION_TARGET.getMessage());

		verify(firebaseMessaging, never()).sendAsync(any());
	}
}
