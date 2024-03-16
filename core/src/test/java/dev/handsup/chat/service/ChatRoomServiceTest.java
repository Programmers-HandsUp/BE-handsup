package dev.handsup.chat.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.chat.domain.ChatMessage;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.response.ChatMessageResponse;
import dev.handsup.chat.dto.response.ChatRoomDetailResponse;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.chat.repository.ChatMessageRepository;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BiddingFixture;
import dev.handsup.fixture.ChatMessageFixture;
import dev.handsup.fixture.ChatRoomFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;

@DisplayName("[채팅방 서비스 단위 테스트]")
@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

	private final User seller = UserFixture.user(1L);
	private final User bidder = UserFixture.user(2L);
	private PageRequest pageRequest = PageRequest.of(0, 5);
	@Mock
	private AuctionRepository auctionRepository;

	@Mock
	private ChatRoomRepository chatRoomRepository;
	@Mock
	private ChatMessageRepository chatMessageRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BiddingRepository biddingRepository;

	@InjectMocks
	private ChatRoomService chatRoomService;

	@DisplayName("[판매자는 입찰자와 채팅방이 없으면, 채팅방을 생성할 수 있다.]")
	@Test
	void registerChatRoom_notExist() {
		//given
		Auction auction = mock(Auction.class);
		Bidding bidding = BiddingFixture.bidding(auction, bidder, TradingStatus.PREPARING);
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(bidding);

		given(biddingRepository.findBiddingWithAuctionAndBidder(anyLong())).willReturn(Optional.of(bidding));

		given(auction.getSeller()).willReturn(seller); //validateAuthorization
		given(auction.getStatus()).willReturn(AuctionStatus.TRADING);
		given(chatRoomRepository.findByAuctionIdAndBidder(1L, bidder))
			.willReturn(Optional.empty());
		given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(chatRoom);

		//when
		RegisterChatRoomResponse response = chatRoomService.registerChatRoom(1L, bidder.getId(), seller);

		//then
		assertThat(response).isNotNull();
	}

	@DisplayName("[판매자는 입찰자와 채팅방이 있으면, 입찰 아이디를 갱신하고 기존 채팅방 아이디를 반환한다.]")
	@Test
	void registerChatRoom_exists() {
		//given
		Auction auction = mock(Auction.class);
		Bidding bidding = BiddingFixture.bidding(auction, bidder, TradingStatus.PREPARING);
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(bidding);

		given(biddingRepository.findBiddingWithAuctionAndBidder(anyLong())).willReturn(Optional.of(bidding));

		given(auction.getSeller()).willReturn(seller); //validateAuthorization
		given(auction.getStatus()).willReturn(AuctionStatus.TRADING);
		given(chatRoomRepository.findByAuctionIdAndBidder(1L, bidder))
			.willReturn(Optional.of(chatRoom));

		//when
		RegisterChatRoomResponse response = chatRoomService.registerChatRoom(1L, bidder.getId(), seller);

		//then
		assertThat(response).isNotNull();
	}

	@DisplayName("[유저로 채팅방을 모두 조회할 수 있다.]")
	@Test
	void getUserChatRooms() {
		//given
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(1L, seller, bidder, 1L);
		given(chatRoomRepository.findChatRoomsByUser(seller, pageRequest))
			.willReturn(new SliceImpl<>(List.of(chatRoom), pageRequest, false));
		//when
		PageResponse<ChatRoomSimpleResponse> response = chatRoomService.getUserChatRooms(seller, pageRequest);

		//then
		assertThat(response.content().get(0)).isNotNull();
	}

	@DisplayName("[채팅방 아이디로 채팅방을 조회할 수 있다.]")
	@Test
	void getChatRoomWithId() {
		//given
		Auction auction = AuctionFixture.auction(seller);
		Bidding bidding = BiddingFixture.bidding(auction, bidder, TradingStatus.PREPARING);
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(bidding);

		given(biddingRepository.findBiddingWithAuctionAndBidder(anyLong())).willReturn(Optional.of(bidding));
		given(chatRoomRepository.findById(chatRoom.getId())).willReturn(Optional.of(chatRoom));

		//when
		ChatRoomDetailResponse response = chatRoomService.getChatRoomWithId(chatRoom.getId(), seller);

		//then
		assertAll(
			() -> assertThat(response.auctionTitle()).isEqualTo(auction.getTitle()),
			() -> assertThat(response.receiverId()).isEqualTo(chatRoom.getBidder().getId()),
			() -> assertThat(response.currentBiddingId()).isEqualTo(chatRoom.getCurrentBiddingId())
		);
	}

	@DisplayName("[입찰 아이디로 채팅방을 조회할 수 있다.]")
	@Test
	void getChatRoomWithBiddingId() {
		//given
		Auction auction = AuctionFixture.auction(seller);
		Bidding bidding = BiddingFixture.bidding(auction, bidder);
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(bidding);

		given(biddingRepository.findBiddingWithAuctionAndBidder(anyLong())).willReturn(Optional.of(bidding));
		given(chatRoomRepository.findChatRoomByCurrentBiddingId(bidding.getId()))
			.willReturn(Optional.of(chatRoom));

		//when
		ChatRoomDetailResponse response = chatRoomService.getChatRoomWithBiddingId(bidding.getId(), seller);

		//then
		assertAll(
			() -> assertThat(response.auctionTitle()).isEqualTo(auction.getTitle()),
			() -> assertThat(response.receiverId()).isEqualTo(chatRoom.getBidder().getId())
		);
	}

	@DisplayName("[채팅방 아이디로 메시지를 슬라이스하여 조회할 수 있다.]")
	@Test
	void getChatRoomMessages() {
		//given
		Auction auction = AuctionFixture.auction(seller);
		Bidding bidding = BiddingFixture.bidding(auction, bidder);
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(bidding);
		ChatMessage chatMessage1 = ChatMessageFixture.chatMessage(chatRoom, seller);
		ChatMessage chatMessage2 = ChatMessageFixture.chatMessage(chatRoom, bidder);

		given(chatRoomRepository.findById(1L)).willReturn(Optional.of(chatRoom));
		given(chatMessageRepository.findByChatRoomOrderByCreatedAt(chatRoom, pageRequest))
			.willReturn(new SliceImpl<>(List.of(chatMessage1, chatMessage2), pageRequest, false));

		//when
		PageResponse<ChatMessageResponse> response = chatRoomService.getChatRoomMessages(1L, pageRequest);
		//then
		assertThat(response.content()).hasSize(2);
	}
}