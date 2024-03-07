package dev.handsup.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import dev.handsup.chat.dto.request.ChatMessageRequest;
import dev.handsup.chat.dto.response.ChatMessageResponse;
import dev.handsup.chat.service.ChatMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "채팅 메시지 API")
@Controller
@RequiredArgsConstructor
public class ChatMessageApiController {

	private final ChatMessageService chatMessageService;

	@MessageMapping("/chat-rooms/{chatRoomId}")
	@SendTo("/queue/chat-rooms/{chatRoomId}")
	public ChatMessageResponse chatMessageOfNewRoom(
		@DestinationVariable Long chatRoomId,
		@Payload ChatMessageRequest request
	) {
		return chatMessageService.registerChatMessage(chatRoomId, request);
	}

}
