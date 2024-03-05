package dev.handsup.chat.service;

import static org.assertj.core.api.Assertions.*;
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
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.chat.repository.ChatRoomRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.fixture.ChatRoomFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;

@DisplayName("[채팅방 서비스 단위 테스트]")
@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

	private PageRequest pageRequest = PageRequest.of(0, 5);
	private final User seller = UserFixture.user(1L);
	private final User buyer = UserFixture.user(2L);

	@Mock
	private AuctionRepository auctionRepository;

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ChatRoomService chatRoomService;

	@DisplayName("[채팅방을 생성할 수 있다.]")
	@Test
	void registerChatRoom() {
		//given
		Auction auction = mock(Auction.class);
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(1L, seller, buyer);

		given(userRepository.findById(buyer.getId())).willReturn(Optional.of(buyer));
		given(auctionRepository.findById(1L)).willReturn(Optional.of(auction));
		given(auction.getStatus()).willReturn(AuctionStatus.TRADING);
		given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(chatRoom);

		//when
		RegisterChatRoomResponse response = chatRoomService.registerChatRoom(1L, buyer.getId(), seller);

		//then
		assertThat(response).isNotNull();
	}

	@DisplayName("[유저로 채팅방을 모두 조회할 수 있다.]")
	@Test
	void getUserChatRooms() {
		//given
		ChatRoom chatRoom = ChatRoomFixture.chatRoom(1L, seller, buyer);
		given(chatRoomRepository.findChatRoomsByUser(seller, pageRequest))
			.willReturn(new SliceImpl<>(List.of(chatRoom), pageRequest, false));
		//when
		PageResponse<ChatRoomSimpleResponse> response = chatRoomService.getUserChatRooms(seller, pageRequest);

		//then
		assertThat(response.content().get(0)).isNotNull();
	}

}