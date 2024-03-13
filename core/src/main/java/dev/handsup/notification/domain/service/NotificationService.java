package dev.handsup.notification.domain.service;

public interface NotificationService {

	void sendChatMessage(String subscriberEmail, String publisherNickname);

	void sendCommentMessage(String subscriberEmail, String publisherNickname);

	void sendBookmarkMessage(String subscriberEmail, String publisherNickname);

	void sendPurchaseWinningMessage(String subscriberEmail);

	void sendCanceledPurchaseWinningMessage(String subscriberEmail);
}
