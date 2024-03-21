package dev.handsup.notification.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.notification.domain.Notification;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.dto.NotificationMapper;
import dev.handsup.notification.dto.response.NotificationResponse;
import dev.handsup.notification.repository.NotificationRepository;
import dev.handsup.user.domain.User;
import dev.handsup.user.exception.UserErrorCode;
import dev.handsup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	private final AuctionRepository auctionRepository;

	public long countNotificationsByUserEmail(String userEmail) {
		return notificationRepository.countByReceiverEmail(userEmail);
	}

	@Transactional
	public void saveNotification(
		String senderEmail,
		String receiverEmail,
		String content,
		NotificationType notificationType,
		Auction auction
	) {
		Notification notification = Notification.of(
			senderEmail, receiverEmail, content, notificationType, auction
		);

		notificationRepository.save(notification);
	}

	@Transactional
	public PageResponse<NotificationResponse> getNotifications(User user, Pageable pageable) {
		Slice<NotificationResponse> notificationResponsePage = notificationRepository
			.findByReceiverEmailOrderByCreatedAtDesc(user.getEmail(), pageable)
			.map(notification -> {
				String senderEmail = notification.getSenderEmail();
				String senderProfileImageUrl = getUserByEmail(senderEmail).getProfileImageUrl();
				Auction auction = getAuctionById(notification.getAuction().getId());

				return NotificationMapper.toNotificationResponse(
					notification.getId(),
					notification.getType(),
					notification.getContent(),
					senderProfileImageUrl,
					auction.getId(),
					auction.getProduct().getImages().get(0).getImageUrl()
				);
			});

		// User 의 readNotificationCount 갱신
		log.info("notificationResponsePage.getSize() = " + notificationResponsePage.getSize());
		user.updateReadNotificationCount(notificationResponsePage.getSize());

		return CommonMapper.toPageResponse(notificationResponsePage);
	}

	private User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_BY_EMAIL));
	}

	private Auction getAuctionById(Long auctionId) {
		return auctionRepository.findById(auctionId)
			.orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_AUCTION));
	}
}
