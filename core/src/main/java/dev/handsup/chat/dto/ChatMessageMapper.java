package dev.handsup.chat.dto;

import static lombok.AccessLevel.*;

import dev.handsup.chat.domain.ChatMessage;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.request.ChatMessageRequest;
import dev.handsup.chat.dto.response.ChatMessageResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChatMessageMapper {

	public static ChatMessage toChatMessage(ChatRoom chatRoom, ChatMessageRequest request) {
		return ChatMessage.of(chatRoom, request.senderId(), request.content());
	}

	public static ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
		return ChatMessageResponse.of(
			chatMessage.getChatRoom().getId(),
			chatMessage.getSenderId(),
			chatMessage.getContent(),
			chatMessage.getCreatedAt().toString()
		);
	}
}
