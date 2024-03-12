package dev.handsup.chat.service;

import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.exception.BiddingErrorCode;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.ChatRoomMapper;
import dev.handsup.chat.dto.response.ChatRoomDetailResponse;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.exception.ChatRoomErrorCode;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final BiddingRepository biddingRepository;

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
		Bidding currentBidding = getBiddingById(chatRoom.getCurrentBiddingId());
		User receiver = getReceiver(user, chatRoom);

		return ChatRoomMapper.toChatRoomDetailResponse(chatRoom, currentBidding, receiver);
	}

	// 입찰자 목록에서 조회
	@Transactional(readOnly = true)
	public ChatRoomDetailResponse getChatRoomWithBiddingId(Long biddingId, User user) {
		Bidding bidding = getBiddingById(biddingId);
		validateAuthorization(user, bidding);
		User receiver = bidding.getBidder();
		ChatRoom chatRoom = getChatRoomByCurrentBidding(bidding);

		return ChatRoomMapper.toChatRoomDetailResponse(chatRoom, bidding, receiver);
	}

	private ChatRoom getChatRoomByCurrentBidding(Bidding currentBidding) {
		return chatRoomRepository.findChatRoomByCurrentBiddingId(currentBidding.getId())
			.orElseThrow(() -> new NotFoundException(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM_BY_BIDDING_ID));
	}

	// 채팅방 조회, 생성은 판매자만 가능하도록
	private void validateAuthorization(User user, Bidding bidding) {
		User seller = bidding.getAuction().getSeller();
		if (!Objects.equals(user.getId(), seller.getId())) { // 조회자와 경매 판매자가 같은지
			throw new ValidationException(ChatRoomErrorCode.CHAT_ROOM_ACCESS_DENIED);
		}
	}

	// 경매가 거래상태인지
	private void validateAuctionTrading(Auction auction) {
		if (auction.getStatus() != AuctionStatus.TRADING) {
			throw new ValidationException(ChatRoomErrorCode.NOT_TRADING_AUCTION);
		}
	}

	private Bidding getBiddingById(Long biddingId) {
		return biddingRepository.findById(biddingId)
			.orElseThrow(() -> new NotFoundException(BiddingErrorCode.NOT_FOUND_BIDDING));
	}

	private ChatRoom getChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new NotFoundException(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM));
	}

	private User getReceiver(User user, ChatRoom chatRoom) {
		return chatRoom.getBidder().equals(user) ? chatRoom.getSeller() : chatRoom.getBidder();
	}
}
