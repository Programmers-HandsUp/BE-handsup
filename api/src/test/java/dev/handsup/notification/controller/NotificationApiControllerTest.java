package dev.handsup.notification.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.auth.service.JwtProvider;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.notification.domain.Notification;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.dto.request.SaveFCMTokenRequest;
import dev.handsup.notification.repository.NotificationRepository;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;

@DisplayName("[Notification 통합 테스트]")
class NotificationApiControllerTest extends ApiTestSupport {

	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtProvider jwtProvider;

	private User receiver;
	private String receiverAccessToken;

	@BeforeEach
	void setUp() {
		receiver = UserFixture.user("receiver@naver.com");
		ReflectionTestUtils.setField(receiver, "readNotificationCount", 1);
		userRepository.save(receiver);
		receiverAccessToken = jwtProvider.createAccessToken(receiver.getId());

		Auction auction = AuctionFixture.auction(receiver);
		productCategoryRepository.save(auction.getProduct().getProductCategory());
		auctionRepository.save(auction);

		Notification notification1 = Notification.of(
			user.getEmail(),
			receiver.getEmail(),
			NotificationType.BOOKMARK.getContent(),
			NotificationType.BOOKMARK,
			auction
		);
		Notification notification2 = Notification.of(
			user.getEmail(),
			receiver.getEmail(),
			NotificationType.CHAT.getContent(),
			NotificationType.CHAT,
			auction
		);
		Notification notification3 = Notification.of(
			user.getEmail(),
			receiver.getEmail(),
			NotificationType.PURCHASE_WINNING.getContent(),
			NotificationType.PURCHASE_WINNING,
			auction
		);
		notificationRepository.saveAll(List.of(notification1, notification2, notification3));
	}

	@Test
	@DisplayName("FCM 토큰을 저장에 성공하면 HttpSatus.Ok 를 받는다")
	void saveFCMToken() throws Exception {
		SaveFCMTokenRequest request = SaveFCMTokenRequest.from("fcmToken123");

		mockMvc.perform(post("/api/notifications/fcm-tokens")
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.content(toJson(request)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("사용자의 미확인 알림 수를 조회한다")
	void countUserNotifications() throws Exception {
		mockMvc.perform(get("/api/notifications/count")
				.header(AUTHORIZATION, "Bearer " + receiverAccessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.count").value(2));
	}

	@Test
	@DisplayName("사용자가 받은 전체 알림을 최근 생성 순으로 조회한다")
	void getUserReviewLabels() throws Exception {
		PageRequest pageRequest = PageRequest.of(0, 5);

		mockMvc.perform(get("/api/notifications")
				.header(AUTHORIZATION, "Bearer " + receiverAccessToken)
				.contentType(APPLICATION_JSON)
				.content(toJson(pageRequest))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()").value(3))
			.andExpect(jsonPath("$.content[0].content").value(NotificationType.PURCHASE_WINNING.getContent()))
			.andExpect(jsonPath("$.content[1].content").value(NotificationType.CHAT.getContent()))
			.andExpect(jsonPath("$.content[2].content").value(NotificationType.BOOKMARK.getContent()));
	}
}
