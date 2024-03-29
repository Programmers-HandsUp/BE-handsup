package dev.handsup.chat.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.exception.BiddingErrorCode;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.ChatMessageMapper;
import dev.handsup.chat.dto.ChatRoomMapper;
import dev.handsup.chat.dto.response.ChatMessageResponse;
import dev.handsup.chat.dto.response.ChatRoomDetailResponse;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.chat.exception.ChatRoomErrorCode;
import dev.handsup.chat.repository.ChatMessageRepository;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final BiddingRepository biddingRepository;

	@Transactional
	public RegisterChatRoomResponse registerChatRoom(Long auctionId, Long biddingId, User user) {
		Bidding bidding = getBiddingWithAuctionAndBidderById(biddingId);
		bidding.getAuction().validateIfSeller(user);
		bidding.getAuction().validateAuctionTrading();

		bidding.updateTradingStatusProgressing();

		ChatRoom chatRoom = chatRoomRepository.findByAuctionIdAndBidder(auctionId, bidding.getBidder())
			.map(existingChatRoom -> { // 한 경매 내 입찰자와 판매자 간의 기존 채팅방 존재 (=중복 입찰자)
				existingChatRoom.updateCurrentBiddingId(biddingId); // 채팅방 내 입찰 아이디 갱신
				return existingChatRoom;
			})
			.orElseGet(() -> { // 채팅방 존재x
				ChatRoom newChatRoom = ChatRoomMapper.toChatRoom(bidding);
				return chatRoomRepository.save(newChatRoom);
			});

		return ChatRoomMapper.toRegisterChatRoomResponse(chatRoom);
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
		Bidding currentBidding = getBiddingWithAuctionById(chatRoom.getCurrentBiddingId());
		User receiver = getReceiver(user, chatRoom);
		return ChatRoomMapper.toChatRoomDetailResponse(chatRoom, currentBidding, receiver);
	}

	// 입찰자 목록에서 조회
	@Transactional(readOnly = true)
	public ChatRoomDetailResponse getChatRoomWithBiddingId(Long biddingId, User user) {
		Bidding bidding = getBiddingWithAuctionAndBidderById(biddingId);
		bidding.getAuction().validateIfSeller(user);
		User receiver = bidding.getBidder();
		ChatRoom chatRoom = getChatRoomByCurrentBidding(bidding);
		return ChatRoomMapper.toChatRoomDetailResponse(chatRoom, bidding, receiver);
	}

	@Transactional(readOnly = true)
	public PageResponse<ChatMessageResponse> getChatRoomMessages(Long chatRoomId, Pageable pageable) {
		ChatRoom chatRoom = getChatRoomById(chatRoomId);
		Slice<ChatMessageResponse> responsePage = chatMessageRepository
			.findByChatRoomOrderByCreatedAtDesc(chatRoom, pageable)
			.map(ChatMessageMapper::toChatMessageResponse);
		return CommonMapper.toPageResponse(responsePage);
	}

	private ChatRoom getChatRoomByCurrentBidding(Bidding currentBidding) {
		return chatRoomRepository.findChatRoomByCurrentBiddingId(currentBidding.getId())
			.orElseThrow(() -> new NotFoundException(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM_BY_BIDDING_ID));
	}

	private Bidding getBiddingWithAuctionAndBidderById(Long biddingId) {
		return biddingRepository.findBiddingWithAuctionAndBidder(biddingId)
			.orElseThrow(() -> new NotFoundException(BiddingErrorCode.NOT_FOUND_BIDDING));
	}

	private Bidding getBiddingWithAuctionById(Long biddingId) {
		return biddingRepository.findBiddingWithAuction(biddingId)
			.orElseThrow(() -> new NotFoundException(BiddingErrorCode.NOT_FOUND_BIDDING));
	}

	private ChatRoom getChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new NotFoundException(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM));
	}

	private User getReceiver(User user, ChatRoom chatRoom) {
		return chatRoom.getBidder().getId().equals(user.getId()) ? chatRoom.getSeller() : chatRoom.getBidder();
	}
}
