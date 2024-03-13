package dev.handsup.notification.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.notification.dto.request.SaveFCMTokenRequest;
import dev.handsup.notification.dto.response.NotificationResponse;
import dev.handsup.notification.service.FCMService;
import dev.handsup.notification.service.NotificationService;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "알림 API")
@RequestMapping("/api/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationApiController {

	private final NotificationService notificationService;
	private final FCMService fcmService;

	@PostMapping("/fcm-tokens")
	@Operation(summary = "FCM 토큰 저장 API", description = "사용자의 Email, FCM 토큰을 key, value 로 저장한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<HttpStatus> saveFCMToken(
		@Parameter(hidden = true) @JwtAuthorization User user,
		@RequestBody SaveFCMTokenRequest request
	) {
		fcmService.saveFcmToken(user.getEmail(), request);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@GetMapping("/count")
	@Operation(summary = "미확인 알림 수 조회 API", description = "특정 사용자의 확인하지 않은 알림 수를 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<Long> countUserNotifications(
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		long readNotificationCount = user.getReadNotificationCount();
		long notificationsCount = notificationService.countNotificationsByUserEmail(user.getEmail());
		return ResponseEntity.ok(notificationsCount - readNotificationCount);
	}

	@GetMapping()
	@Operation(summary = "알림 전체 조회 API", description = "특정 사용자의 알림을 전체 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<PageResponse<NotificationResponse>> getUserReviewLabels(
		@Parameter(hidden = true) @JwtAuthorization User user,
		Pageable pageable
	) {
		PageResponse<NotificationResponse> response = notificationService.getNotifications(user, pageable);
		return ResponseEntity.ok(response);
	}

}
