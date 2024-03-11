package dev.handsup.notification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	long countByReceiverEmail(String receiverEmail);
}
