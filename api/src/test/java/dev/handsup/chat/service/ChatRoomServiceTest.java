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
import dev.handsup.auction.dto.response.ChatRoomExistenceResponse;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.response.ChatRoomDetailResponse;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BiddingFixture;
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
	private UserRepository userRepository;

	@Mock
	private BiddingRepository biddingRepository;

	@InjectMocks
	private ChatRoomService chatRoomService;

	@DisplayName("[채팅방을 생성할 수 있다.]")
	@Test
	void registerChatRoom() {
		//given
		Auction auction = mock(Auction.class);
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(1L, seller, bidder);

		given(userRepository.findById(bidder.getId())).willReturn(Optional.of(bidder));
		given(auctionRepository.findById(1L)).willReturn(Optional.of(auction));
		given(auction.getStatus()).willReturn(AuctionStatus.TRADING);
		given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(chatRoom);

		//when
		RegisterChatRoomResponse response = chatRoomService.registerChatRoom(1L, bidder.getId(), seller);

		//then
		assertThat(response).isNotNull();
	}

	@DisplayName("[유저로 채팅방을 모두 조회할 수 있다.]")
	@Test
	void getUserChatRooms() {
		//given
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(1L, seller, bidder);
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
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(1L, seller, bidder);
		given(chatRoomRepository.findById(chatRoom.getId())).willReturn(Optional.of(chatRoom));
		given(auctionRepository.findById(auction.getId())).willReturn(Optional.of(auction));

		//when
		ChatRoomDetailResponse response = chatRoomService.getChatRoomWithId(seller, chatRoom.getId());

		//then
		assertAll(
			() -> assertThat(response.auctionTitle()).isEqualTo(auction.getTitle()),
			() -> assertThat(response.receiverNickName()).isEqualTo(chatRoom.getSeller().getNickname())
		);
	}

	@DisplayName("[입찰 아이디로 채팅방을 조회할 수 있다.]")
	@Test
	void getChatRoomWithBiddingId() {
		//given
		Auction auction = AuctionFixture.auction(seller);
		Bidding bidding = BiddingFixture.bidding(auction, bidder);
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(1L, seller, bidder);

		given(biddingRepository.findById(bidding.getId())).willReturn(Optional.of(bidding));
		given(chatRoomRepository.findChatRoomByAuctionIdAndBidder(auction.getId(), bidder))
			.willReturn(Optional.of(chatRoom));

		//when
		ChatRoomDetailResponse response = chatRoomService.getChatRoomWithBiddingId(seller, bidding.getId());

		//then
		assertAll(
			() -> assertThat(response.auctionTitle()).isEqualTo(auction.getTitle()),
			() -> assertThat(response.receiverNickName()).isEqualTo(chatRoom.getSeller().getNickname())
		);
	}

	@DisplayName("[판매자와 입찰자 간의 채팅방이 있는지 확인한다.]")
	@Test
	void getChatRoomExistence() {
		//given
		Auction auction = AuctionFixture.auction(seller);
		Bidding bidding = BiddingFixture.bidding(auction, bidder);

		given(biddingRepository.findById(bidding.getId())).willReturn(Optional.of(bidding));
		given(chatRoomRepository.existsByAuctionIdAndBidder(bidding.getAuction().getId(), bidding.getBidder())).willReturn(true);

		//when
		ChatRoomExistenceResponse response = chatRoomService.getChatRoomExistence(seller, bidding.getId());

		//then
		assertThat(response.isExist()).isTrue();
	}

}