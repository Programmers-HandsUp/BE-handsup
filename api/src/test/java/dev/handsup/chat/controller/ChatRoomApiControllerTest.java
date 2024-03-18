package dev.handsup.chat.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.chat.domain.ChatMessage;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.exception.ChatRoomErrorCode;
import dev.handsup.chat.repository.ChatMessageRepository;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BiddingFixture;
import dev.handsup.fixture.ChatMessageFixture;
import dev.handsup.fixture.ChatRoomFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

@DisplayName("[ChatRoom 통합 테스트]")
class ChatRoomApiControllerTest extends ApiTestSupport {

	private User seller, bidder;
	private ProductCategory productCategory;
	private Auction auction;
	private Bidding bidding;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private BiddingRepository biddingRepository;
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@BeforeEach
	void setUp() {
		seller = user;
		bidder = UserFixture.user2();
		userRepository.saveAll(List.of(bidder, seller));

		productCategory = ProductFixture.productCategory("디지털 기기");
		productCategoryRepository.save(productCategory);

		auction = AuctionFixture.auction(seller, productCategory);
		ReflectionTestUtils.setField(auction, "status", AuctionStatus.TRADING);
		auctionRepository.save(auction);

		biddingRepository.save(bidding = BiddingFixture.bidding(auction, bidder));
	}

	@DisplayName("[기존 채팅방이 없으면, 채팅방을 생성할 수 있다.]")
	@Test
	void registerChatRoom_not_exists() throws Exception {
		//when then
		mockMvc.perform(post("/api/auctions/chat-rooms")
				.param("auctionId", auction.getId().toString())
				.param("biddingId", bidding.getId().toString())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.chatRoomId").isNumber())
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("[동일한 경매 아이디, 구매자의 채팅방이 있을 경우 기존 채팅방 아이디를 반환한다.]")
	@Test
	void registerChatRoom_exists() throws Exception {
		//given
		Bidding secondBidding = BiddingFixture.bidding(auction, bidder);
		biddingRepository.save(secondBidding);
		ChatRoom existedChatRoom = chatRoomRepository.save(ChatRoomFixture.chatRoom(bidding));
		//when then
		mockMvc.perform(post("/api/auctions/chat-rooms")
				.param("auctionId", auction.getId().toString())
				.param("biddingId", secondBidding.getId().toString())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.chatRoomId").value(existedChatRoom.getId()));
	}

	@DisplayName("[경매가 거래 상태가 아니면 채팅방을 생성할 수 없다.]")
	@Test
	void registerChatRoom_fails() throws Exception {
		userRepository.saveAll(List.of(bidder, seller));
		ReflectionTestUtils.setField(auction, "status", AuctionStatus.BIDDING);
		auctionRepository.save(auction);

		//when then
		mockMvc.perform(post("/api/auctions/chat-rooms")
				.param("auctionId", auction.getId().toString())
				.param("biddingId", bidding.getId().toString())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(ChatRoomErrorCode.NOT_TRADING_AUCTION.getMessage()))
			.andExpect(jsonPath("$.code").value(ChatRoomErrorCode.NOT_TRADING_AUCTION.getCode()));
	}

	@DisplayName("[유저가 속한 채팅방을 모두 조회할 수 있다.]")
	@Test
	void getUserChatRooms() throws Exception {
		//given
		User unrelatedUser = UserFixture.user(3L, "unrelatedUser@gmail.com");
		userRepository.save(unrelatedUser);

		Auction auction2 = AuctionFixture.auction(productCategory);
		auctionRepository.save(auction2);

		ChatRoom chatRoom1 = ChatRoomFixture.chatRoom(auction.getId(), seller, bidder, bidding.getId());
		ChatRoom chatRoom2 = ChatRoomFixture.chatRoom(auction.getId(), unrelatedUser, bidder, bidding.getId());
		ChatRoom chatRoom3 = ChatRoomFixture.chatRoom(auction2.getId(), seller, unrelatedUser, bidding.getId());
		chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2, chatRoom3));

		mockMvc.perform(get("/api/auctions/chat-rooms")
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].chatRoomId").value(chatRoom3.getId()))
			.andExpect(jsonPath("$.content[0].receiverNickName").value(seller.getNickname()))
			.andExpect(jsonPath("$.content[0].receiverImageUrl").value(seller.getProfileImageUrl()))
			.andExpect(jsonPath("$.content[1].chatRoomId").value(chatRoom1.getId()))
			.andExpect(jsonPath("$.content[1].receiverNickName").value(bidder.getNickname()))
			.andExpect(jsonPath("$.content[1].receiverImageUrl").value(bidder.getProfileImageUrl()))
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("[채팅방 아이디로 채팅방을 상세 조회할 수 있다.]")
	@Test
	void getChatRoomWithId() throws Exception {
		//given
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(bidding);
		chatRoomRepository.save(chatRoom);

		//when, then
		mockMvc.perform(get("/api/auctions/chat-rooms/{chatRoomId}", chatRoom.getId())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.chatRoomId").value(chatRoom.getId()))
			.andExpect(jsonPath("$.auctionId").value(auction.getId()))
			.andExpect(jsonPath("$.currentBiddingId").value(bidding.getId()))
			.andExpect(jsonPath("$.auctionTitle").value(auction.getTitle()))
			.andExpect(jsonPath("$.receiverId").value(bidder.getId()))
			.andExpect(jsonPath("$.receiverNickName").value(bidder.getNickname()))
			.andExpect(jsonPath("$.receiverScore").value(bidder.getScore()))
			.andExpect(jsonPath("$.receiverProfileImageUrl").value(bidder.getProfileImageUrl()));
	}

	@DisplayName("[채팅방 아이디에 해당하는 채팅방이 없으면 예외를 반환한다.]")
	@Test
	void getChatRoomWithId_fails() throws Exception {
		//when, then
		mockMvc.perform(get("/api/auctions/chat-rooms/{chatRoomId}", 1L)
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code")
				.value(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM.getCode()))
			.andExpect(jsonPath("$.message")
				.value(ChatRoomErrorCode.NOT_FOUND_CHAT_ROOM.getMessage()));
	}

	@DisplayName("[입찰 아이디로 채팅방을 상세 조회할 수 있다.]")
	@Test
	void getChatRoomWithBiddingId() throws Exception {
		//given
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(bidding);
		chatRoomRepository.save(chatRoom);
		//when, then
		mockMvc.perform(get("/api/auctions/chat-rooms/biddings/{biddingId}", bidding.getId())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.chatRoomId").value(chatRoom.getId()))
			.andExpect(jsonPath("$.auctionId").value(auction.getId()))
			.andExpect(jsonPath("$.auctionTitle").value(auction.getTitle()))
			.andExpect(jsonPath("$.receiverId").value(bidder.getId()))
			.andExpect(jsonPath("$.receiverNickName").value(bidder.getNickname()))
			.andExpect(jsonPath("$.receiverScore").value(bidder.getScore()))
			.andExpect(jsonPath("$.receiverProfileImageUrl").value(bidder.getProfileImageUrl()));
	}

	@DisplayName("[해당 경매의 판매자가 아니면 입찰 아이디로 채팅방을 조회할 수 없다.]")
	@Test
	void getChatRoomWithBiddingId_fails() throws Exception {
		//given
		User unrelatedUser = UserFixture.user(3L, "unrelatedUser@gmail.com");
		userRepository.save(unrelatedUser);
		Auction unrelatedUserAuction = auctionRepository.save(AuctionFixture.auction(unrelatedUser, productCategory));
		auctionRepository.save(unrelatedUserAuction);

		Bidding bidding = BiddingFixture.bidding(unrelatedUserAuction, bidder);
		biddingRepository.save(bidding);
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(unrelatedUser, bidding);
		chatRoomRepository.save(chatRoom);

		//when, then
		mockMvc.perform(get("/api/auctions/chat-rooms/biddings/{biddingId}", bidding.getId())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("[채팅방 아이디로 채팅 메시지를 모두 조회할 수 있다.]")
	@Test
	void getChatRoomMessages() throws Exception {
		//given
		User user1 = userRepository.save(UserFixture.user1());
		Bidding bidding2 = biddingRepository.save(BiddingFixture.bidding(auction, user1));
		ChatRoom chatRoom1 = ChatRoomFixture.chatRoom(seller, bidding);
		ChatRoom chatRoom2 = ChatRoomFixture.chatRoom(seller, bidding2);
		chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2));

		ChatMessage chatMessage1 = ChatMessageFixture.chatMessage(chatRoom1, bidder);
		ChatMessage chatMessage2 = ChatMessageFixture.chatMessage(chatRoom1, seller);
		ChatMessage otherChatMessage3 = ChatMessageFixture.chatMessage(chatRoom2, seller);
		chatMessageRepository.saveAll(List.of(chatMessage1, chatMessage2, otherChatMessage3));

		//when, then
		mockMvc.perform(get("/api/auctions/chat-rooms/{chatRoomId}/messages", chatRoom1.getId())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(2))
			.andExpect(jsonPath("$.content[0].chatRoomId").value(chatMessage1.getChatRoom().getId()))
			.andExpect(jsonPath("$.content[0].content").value(chatMessage1.getContent()))
			.andExpect(jsonPath("$.content[0].senderId").value(chatMessage1.getSenderId()))
			.andExpect(jsonPath("$.content[0].content").value(chatMessage1.getContent()))
			.andExpect(jsonPath("$.content[1].chatRoomId").value(chatMessage2.getChatRoom().getId()))
			.andExpect(jsonPath("$.content[1].content").value(chatMessage2.getContent()))
			.andExpect(jsonPath("$.content[1].senderId").value(chatMessage2.getSenderId()))
			.andExpect(jsonPath("$.content[1].content").value(chatMessage2.getContent()));
	}
}
