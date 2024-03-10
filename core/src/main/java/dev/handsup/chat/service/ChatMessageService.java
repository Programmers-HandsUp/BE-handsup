package dev.handsup.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.chat.domain.ChatMessage;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.ChatMessageMapper;
import dev.handsup.chat.dto.request.ChatMessageRequest;
import dev.handsup.chat.dto.response.ChatMessageResponse;
import dev.handsup.chat.exception.ChatRoomErrorCode;
import dev.handsup.chat.repository.ChatMessageRepository;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public ChatMessageResponse registerChatMessage(Long chatRoomId, ChatMessageRequest request) {
		ChatRoom chatRoom = getChatRoomById(chatRoomId);

		ChatMessage chatMessage = ChatMessageMapper.toChatMessage(chatRoom, request);
		ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);
		return ChatMessageMapper.toChatMessageResponse(savedChatMessage);
	}

	private ChatRoom getChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new NotFoundException(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM));
	}
}
