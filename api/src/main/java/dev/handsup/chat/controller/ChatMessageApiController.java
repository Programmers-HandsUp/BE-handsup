package dev.handsup.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import dev.handsup.chat.MessagePublisher;
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
	private final MessagePublisher messagePublisher;

	@MessageMapping("/chat-rooms/{chatRoomId}")
	public void chatMessage(@DestinationVariable Long chatRoomId, @Payload ChatMessageRequest request) {
		ChatMessageResponse response = chatMessageService.registerChatMessage(chatRoomId, request);
		messagePublisher.publish("/sub/chat-rooms/" + chatRoomId, response);
	}

}
