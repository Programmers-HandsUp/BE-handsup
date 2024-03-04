package dev.handsup.chat.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.ChatMapper;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.dto.PageResponse;
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

	public RegisterChatRoomResponse registerChatRoom(Long auctionId, Long buyerId, User seller){
		ChatRoom chatRoom = ChatMapper.toChatRoom(auctionId, seller, getBuyer(buyerId));
		return ChatMapper.toRegisterChatRoomResponse(chatRoomRepository.save(chatRoom));
	}

	@Transactional(readOnly = true)
	public PageResponse<ChatRoomSimpleResponse> getUserChatRooms(User user, Pageable pageable) {
		Slice<ChatRoomSimpleResponse> chatRoomResponses = chatRoomRepository.findChatRoomsByUser(user, pageable)
			.map(chatRoom -> {
				User receiver = chatRoom.getBuyer().equals(user) ? chatRoom.getSeller() : chatRoom.getBuyer();
				return ChatMapper.toChatRoomSimpleResponse(chatRoom, receiver);
			});
		return ChatMapper.toPageResponse(chatRoomResponses);
	}

	private User getBuyer(Long buyerId) {
		return userRepository.findById(buyerId).orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_BY_ID));
	}
}
