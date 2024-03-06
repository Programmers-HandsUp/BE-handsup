package dev.handsup.chat.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.dto.response.ChatRoomExistenceResponse;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.exception.BiddingErrorCode;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.ChatRoomMapper;
import dev.handsup.chat.dto.response.ChatRoomDetailResponse;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.chat.exception.ChatRoomErrorCode;
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
	private final BiddingRepository biddingRepository;

	public RegisterChatRoomResponse registerChatRoom(Long auctionId, Long bidderId, User seller) {
		User bidder = getUserById(bidderId);
		validateAuthorization(seller, auctionId);
		validateAuctionTrading(auctionId);
		validateChatRoomNotExists(auctionId, bidder);
		ChatRoom chatRoom = ChatRoomMapper.toChatRoom(auctionId, seller, bidder);

		return ChatRoomMapper.toRegisterChatRoomResponse(chatRoomRepository.save(chatRoom));
	}

	@Transactional(readOnly = true)
	public PageResponse<ChatRoomSimpleResponse> getUserChatRooms(User user, Pageable pageable) {
		Slice<ChatRoomSimpleResponse> chatRoomResponses = chatRoomRepository.findChatRoomsByUser(user, pageable)
			.map(chatRoom -> {
				User receiver = getReceiver(user, chatRoom);
				return ChatRoomMapper.toChatRoomSimpleResponse(chatRoom, receiver);
			});
		return CommonMapper.toPageResponse(chatRoomResponses);
	}

	// 채팅 목록에서 조회
	@Transactional(readOnly = true)
	public ChatRoomDetailResponse getChatRoomWithId(Long chatRoomId, User user) {
		ChatRoom chatRoom = getChatRoomById(chatRoomId);
		Auction auction = getAuctionById(chatRoom.getAuctionId());
		User receiver = getReceiver(user, chatRoom);

		return ChatRoomMapper.toChatRoomDetailResponse(chatRoom, auction, receiver);
	}

	// 입찰자 목록에서 조회
	@Transactional(readOnly = true)
	public ChatRoomDetailResponse getChatRoomWithBiddingId(Long biddingId, User seller) {
		Bidding bidding = getBiddingById(biddingId);
		validateAuthorization(seller, bidding);
		Auction auction = bidding.getAuction();
		User bidder = bidding.getBidder();
		ChatRoom chatRoom = getChatRoomByAuctionIdAndBidder(auction.getId(), bidder);

		return ChatRoomMapper.toChatRoomDetailResponse(chatRoom, auction, bidder);
	}

	// 입찰자 목록에서 채팅방 존재 여부 조회
	@Transactional(readOnly = true)
	public ChatRoomExistenceResponse getChatRoomExistence(Long biddingId, User seller) {
		Bidding bidding = getBiddingById(biddingId);
		validateAuthorization(seller, bidding);
		return ChatRoomMapper.toChatRoomExistenceResponse(
			isChatRoomExistsByAuctionIdAndBidder(bidding)
		);
	}

	private Boolean isChatRoomExistsByAuctionIdAndBidder(Bidding bidding) {
		return chatRoomRepository.existsByAuctionIdAndBidder(bidding.getAuction().getId(), bidding.getBidder());
	}

	private ChatRoom getChatRoomByAuctionIdAndBidder(Long auctionId, User bidder) {
		return chatRoomRepository.findChatRoomByAuctionIdAndBidder(auctionId, bidder)
			.orElseThrow(() -> new NotFoundException(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM));
	}

	// 채팅방 조회, 생성은 판매자만 가능하도록
	private void validateAuthorization(User seller, Bidding bidding) {
		if (seller != bidding.getAuction().getSeller()) { // 조회자와 경매 판매자가 같은지
			throw new ValidationException(ChatRoomErrorCode.CHAT_ROOM_ACCESS_DENIED);
		}
	}

	private void validateAuthorization(User seller, Long auctionId) {
		Auction auction = getAuctionById(auctionId);
		if (seller != auction.getSeller()) { // 조회자와 경매 판매자가 같은지
			throw new ValidationException(ChatRoomErrorCode.CHAT_ROOM_ACCESS_DENIED);
		}
	}

	// 경매가 거래상태인지
	private void validateAuctionTrading(Long auctionId) {
		Auction auction = getAuctionById(auctionId);
		if (auction.getStatus() != AuctionStatus.TRADING) {
			throw new ValidationException(ChatRoomErrorCode.NOT_TRADING_AUCTION);
		}
	}

	private void validateChatRoomNotExists(Long auctionId, User bidder) {
		if (chatRoomRepository.existsByAuctionIdAndBidder(auctionId, bidder)) {
			throw new ValidationException(ChatRoomErrorCode.CHAT_ROOM_ALREADY_EXISTS);
		}
	}

	private Auction getAuctionById(Long auctionId) {
		return auctionRepository.findById(auctionId)
			.orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_AUCTION));
	}

	private Bidding getBiddingById(Long biddingId) {
		return biddingRepository.findById(biddingId)
			.orElseThrow(() -> new NotFoundException(BiddingErrorCode.NOT_FOUND_BIDDING));
	}

	private User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_USER));
	}

	private ChatRoom getChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new NotFoundException(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM));
	}

	private User getReceiver(User user, ChatRoom chatRoom) {
		return chatRoom.getBidder().equals(user) ? chatRoom.getSeller() : chatRoom.getBidder();
	}
}

