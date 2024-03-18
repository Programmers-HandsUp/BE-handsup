package dev.handsup.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.chat.domain.ChatMessage;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.ChatMessageMapper;
import dev.handsup.chat.dto.request.ChatMessageRequest;
import dev.handsup.chat.dto.response.ChatMessageResponse;
import dev.handsup.chat.exception.ChatRoomErrorCode;
import dev.handsup.chat.repository.ChatMessageRepository;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.service.FCMService;
import dev.handsup.user.domain.User;
import dev.handsup.user.exception.UserErrorCode;
import dev.handsup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final UserRepository userRepository;
	private final AuctionRepository auctionRepository;
	private final FCMService fcmService;

	@Transactional
	public ChatMessageResponse registerChatMessage(Long chatRoomId, ChatMessageRequest request) {
		ChatRoom chatRoom = getChatRoomById(chatRoomId);

		ChatMessage chatMessage = ChatMessageMapper.toChatMessage(chatRoom, request);
		ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

		User sender = getUserById(request.senderId());
		User receiver;

		if (chatRoom.getSeller().equals(sender)) {
			receiver = chatRoom.getBidder();
		} else {
			receiver = chatRoom.getSeller();
		}

		fcmService.sendMessage(
			sender.getEmail(),
			sender.getNickname(),
			receiver.getEmail(),
			NotificationType.BOOKMARK,
			getAuctionById(chatRoom.getAuctionId())
		);

		return ChatMessageMapper.toChatMessageResponse(savedChatMessage);
	}

	private ChatRoom getChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new NotFoundException(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM));
	}

	private User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_USER));
	}

	public Auction getAuctionById(Long auctionId) {
		return auctionRepository.findById(auctionId)
			.orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_AUCTION));
	}
}
