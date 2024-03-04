package dev.handsup.chat.service;

import org.springframework.stereotype.Service;

import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.ChatMapper;
import dev.handsup.chat.dto.request.RegisterChatRoomResponse;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.user.domain.User;
import dev.handsup.user.exception.UserErrorCode;
import dev.handsup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;

	public RegisterChatRoomResponse registerChatRoom(User seller, Long buyerId ){
		ChatRoom chatRoom = ChatMapper.toChatRoom(seller, getBuyer(buyerId));
		return ChatMapper.toRegisterChatRoomResponse(chatRoomRepository.save(chatRoom));
	}

	private User getBuyer(Long buyerId) {
		return userRepository.findById(buyerId).orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_BY_ID));
	}
}
