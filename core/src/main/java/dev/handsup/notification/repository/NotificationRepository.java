package dev.handsup.notification.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	long countByReceiverEmail(String receiverEmail);

	Slice<Notification> findByReceiverEmailOrderByCreatedAtDesc(String receiverEmail, Pageable pageable);

	void deleteByCreatedAtBefore(LocalDateTime dateTime);
}
