package dev.handsup.notification.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.notification.domain.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "알림 API")
@RequestMapping("/api/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationApiController {

	private final NotificationService notificationService;

	//메시지 조회
}
