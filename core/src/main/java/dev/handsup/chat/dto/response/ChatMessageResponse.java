package dev.handsup.chat.dto.response;

import dev.handsup.chat.domain.ChatMessage;

public record ChatMessageResponse(
	Long chatRoomId,
	Long senderId,
	String content,
	String createdAt
) {
	public static ChatMessageResponse from(ChatMessage chatMessage) {
		return new ChatMessageResponse(
			chatMessage.getChatRoom().getId(),
			chatMessage.getSenderId(),
			chatMessage.getContent(),
			chatMessage.getCreatedAt().toString()
		);
	}
}
