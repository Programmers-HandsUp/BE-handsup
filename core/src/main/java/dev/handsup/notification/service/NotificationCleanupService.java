package dev.handsup.notification.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationCleanupService {

	private final NotificationRepository notificationRepository;

	// 매일 자정에 실행
	@Transactional
	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteOldNotifications() {
		notificationRepository.deleteByCreatedAtBefore(LocalDateTime.now().minusWeeks(2));
	}
}
