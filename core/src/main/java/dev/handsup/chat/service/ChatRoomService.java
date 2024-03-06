package dev.handsup.chat.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.ChatMapper;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.chat.exception.ChatErrorCode;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.user.domain.User;
import dev.handsup.user.exception.UserErrorCode;
import dev.handsup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final AuctionRepository auctionRepository;

	public RegisterChatRoomResponse registerChatRoom(Long auctionId, Long bidderId, User seller) {
		User bidder = getUserById(bidderId);
		validateIfAuctionTrading(auctionId);
		validateIfChatRoomNotExists(auctionId, bidder);
		ChatRoom chatRoom = ChatMapper.toChatRoom(auctionId, seller, bidder);

		return ChatMapper.toRegisterChatRoomResponse(chatRoomRepository.save(chatRoom));
	}

	@Transactional(readOnly = true)
	public PageResponse<ChatRoomSimpleResponse> getUserChatRooms(User user, Pageable pageable) {
		Slice<ChatRoomSimpleResponse> chatRoomResponses = chatRoomRepository.findChatRoomsByUser(user, pageable)
			.map(chatRoom -> {
				User receiver = chatRoom.getBidder().equals(user) ? chatRoom.getSeller() : chatRoom.getBidder();
				return ChatMapper.toChatRoomSimpleResponse(chatRoom, receiver);
			});
		return CommonMapper.toPageResponse(chatRoomResponses);
	}

	private void validateIfAuctionTrading(Long auctionId) {
		Auction auction = getAuctionById(auctionId);
		if (auction.getStatus() != AuctionStatus.TRADING) {
			throw new ValidationException(ChatErrorCode.NOT_TRADING_AUCTION);
		}
	}

	private void validateIfChatRoomNotExists(Long auctionId, User bidder) {
		if (chatRoomRepository.existsByAuctionIdAndBidder(auctionId, bidder)) {
			throw new ValidationException(ChatErrorCode.CHAT_ROOM_ALREADY_EXISTS);
		}
	}

	public Auction getAuctionById(Long auctionId) {
		return auctionRepository.findById(auctionId)
			.orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_AUCTION));
	}

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_USER));
	}
}
