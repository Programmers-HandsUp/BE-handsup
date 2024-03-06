package dev.handsup.controller;

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
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.exception.ChatErrorCode;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ChatRoomFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

@DisplayName("[ChatRoom 통합 테스트]")
class ChatRoomApiControllerTest extends ApiTestSupport {

	private final User loginUser = UserFixture.user();
	private final User seller = UserFixture.user("seller@gmail.com");
	private final User bidder = UserFixture.user("bidder@gmail.com");
	private ProductCategory productCategory;
	private Auction auction;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@BeforeEach
	void setUp() {
		productCategory = ProductFixture.productCategory("디지털 기기");
		productCategoryRepository.save(productCategory);
		auction = AuctionFixture.auction(productCategory);
		ReflectionTestUtils.setField(auction, "status", AuctionStatus.TRADING);
		auctionRepository.save(auction);
	}

	@DisplayName("[채팅방을 생성할 수 있다.]")
	@Test
	void registerChatRoom() throws Exception {
		userRepository.saveAll(List.of(bidder, seller));
		//when then
		mockMvc.perform(post("/api/auctions/chat-rooms")
				.param("auctionId", auction.getId().toString())
				.param("bidderId", bidder.getId().toString())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.chatRoomId").isNumber())
			.andDo(MockMvcResultHandlers.print());
	}

	@Transactional
	@DisplayName("[경매가 거래 상태가 아니면 채팅방을 생성할 수 없다.]")
	@Test
	void registerChatRoom_fails() throws Exception {
		userRepository.saveAll(List.of(bidder, seller));
		//when then
		ReflectionTestUtils.setField(auction, "status", AuctionStatus.BIDDING);
		mockMvc.perform(post("/api/auctions/chat-rooms")
				.param("auctionId", auction.getId().toString())
				.param("bidderId", bidder.getId().toString())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(ChatErrorCode.NOT_TRADING_AUCTION.getMessage()))
			.andExpect(jsonPath("$.code").value(ChatErrorCode.NOT_TRADING_AUCTION.getCode()));
	}

	@DisplayName("[동일한 경매 아이디, 구매자의 채팅방이 있을 경우 채팅방을 생성할 수 없다.]")
	@Test
	void registerChatRoom_fails2() throws Exception {
		//given
		userRepository.saveAll(List.of(bidder, seller));
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(auction.getId(), seller, bidder);
		chatRoomRepository.save(chatRoom);
		//when then
		mockMvc.perform(post("/api/auctions/chat-rooms")
				.param("auctionId", auction.getId().toString())
				.param("bidderId", bidder.getId().toString())
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(ChatErrorCode.CHAT_ROOM_ALREADY_EXISTS.getMessage()))
			.andExpect(jsonPath("$.code").value(ChatErrorCode.CHAT_ROOM_ALREADY_EXISTS.getCode()));
	}

	@DisplayName("[유저가 속한 채팅방을 모두 조회할 수 있다.]")
	@Test
	void getUserChatRooms() throws Exception {
		//given
		User user1 = UserFixture.user("user1@gmail.com");
		User user2 = UserFixture.user("user2@gmail.com");
		userRepository.saveAll(List.of(loginUser, user1, user2));

		Auction auction2 = AuctionFixture.auction(productCategory);
		auctionRepository.save(auction2);

		ChatRoom chatRoom1 = ChatRoomFixture.chatRoom(auction.getId(), user1, loginUser);
		ChatRoom chatRoom2 = ChatRoomFixture.chatRoom(auction.getId(), user1, user2);
		ChatRoom chatRoom3 = ChatRoomFixture.chatRoom(auction2.getId(), user2, loginUser);
		chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2, chatRoom3));

		mockMvc.perform(get("/api/auctions/chat-rooms")
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].chatRoomId").value(chatRoom1.getId()))
			.andExpect(jsonPath("$.content[0].receiverNickName").value(user1.getNickname()))
			.andExpect(jsonPath("$.content[0].receiverImageUrl").value(user1.getProfileImageUrl()))
			.andExpect(jsonPath("$.content[1].chatRoomId").value(chatRoom3.getId()))
			.andExpect(jsonPath("$.content[1].receiverNickName").value(user2.getNickname()))
			.andExpect(jsonPath("$.content[1].receiverImageUrl").value(user2.getProfileImageUrl()))
			.andDo(MockMvcResultHandlers.print());
	}
}