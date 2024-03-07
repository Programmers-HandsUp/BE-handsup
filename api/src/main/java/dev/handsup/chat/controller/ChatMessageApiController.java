package dev.handsup.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.chat.dto.request.ChatMessageRequest;
import dev.handsup.chat.dto.response.ChatMessageResponse;
import dev.handsup.chat.service.ChatMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "채팅 메시지 API")
@RestController
@RequiredArgsConstructor
public class ChatMessageApiController {

	private final ChatMessageService chatMessageService;

	@MessageMapping("/chat-rooms/{chatRoomId}")
	@SendTo("/queue/chat-rooms/{chatRoomId}")
	public ResponseEntity<ChatMessageResponse> chatMessageOfNewRoom(
		@DestinationVariable Long chatRoomId,
		@Payload ChatMessageRequest request
	) {
		ChatMessageResponse response = chatMessageService.registerChatMessage(chatRoomId, request);
		return ResponseEntity.ok(response);
	}

}
