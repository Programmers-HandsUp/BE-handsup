package dev.handsup.chat.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.common.support.DataJpaTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BiddingFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;

@DisplayName("[ChatRoomRepository 테스트]")
class ChatRoomRepositoryTest extends DataJpaTestSupport {
	private final PageRequest pageRequest = PageRequest.of(0, 10);
	private User user1, user2, user3;
	private Auction auction1, auction2;
	private Bidding bidding1, bidding2, bidding3, bidding4;
	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private BiddingRepository biddingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@BeforeEach
	void setUp() {
		ProductCategory category = ProductFixture.productCategory("디지털 기기");
		productCategoryRepository.save(category);

		user1 = UserFixture.user1();
		user2 = UserFixture.user2();
		user3 = UserFixture.user(3L, "user3@naver.com");
		userRepository.saveAll(List.of(user1, user2, user3));

		auction1 = AuctionFixture.auction(category);
		auction2 = AuctionFixture.auction(category);
		auctionRepository.saveAll(List.of(auction1, auction2));

		bidding1 = BiddingFixture.bidding(auction1, user1, TradingStatus.PREPARING);
		bidding2 = BiddingFixture.bidding(auction1, user2, TradingStatus.CANCELED);
		bidding3 = BiddingFixture.bidding(auction1, user3, TradingStatus.COMPLETED);
		bidding4 = BiddingFixture.bidding(auction1, user3, TradingStatus.COMPLETED);
		biddingRepository.saveAll(List.of(bidding1, bidding2, bidding3, bidding4));
	}

	@DisplayName("[유저가 속한 채팅방을 모두 조회할 수 있다.]")
	@Test
	void findChatRoomsByUser() {
		//given
		ChatRoom chatRoom1 = ChatRoom.of(auction1.getId(), user1, user2, bidding2.getId());
		ChatRoom chatRoom2 = ChatRoom.of(auction2.getId(), user2, user1, bidding1.getId());
		ChatRoom chatRoom3 = ChatRoom.of(auction2.getId(), user1, user3, bidding3.getId());
		ChatRoom chatRoomWithoutUser1 = ChatRoom.of(auction2.getId(), user2, user3, bidding4.getId());
		chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2, chatRoom3, chatRoomWithoutUser1));
		//when
		Slice<ChatRoom> chatRoomSlice = chatRoomRepository.findChatRoomsByUser(user1, pageRequest);
		List<ChatRoom> chatRooms = chatRoomSlice.getContent();

		//then
		assertAll(
			() -> assertThat(chatRoomSlice).hasSize(3),
			() -> assertThat(chatRoomSlice.hasNext()).isFalse(),
			() -> assertThat(chatRooms.get(0)).isEqualTo(chatRoom1),
			() -> assertThat(chatRooms.get(1)).isEqualTo(chatRoom2),
			() -> assertThat(chatRooms.get(2)).isEqualTo(chatRoom3)
		);
	}
}
