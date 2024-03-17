package dev.handsup.chat.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.chat.domain.ChatMessage;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.common.support.DataJpaTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BiddingFixture;
import dev.handsup.fixture.ChatMessageFixture;
import dev.handsup.fixture.ChatRoomFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;

class ChatMessageRepositoryTest extends DataJpaTestSupport {

	private User bidder, seller;
	private ChatRoom chatRoom;

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

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
		seller = userRepository.save(UserFixture.user1());
		bidder = userRepository.save(UserFixture.user2());
		ProductCategory category = productCategoryRepository.save(ProductFixture.productCategory("디지털 기기"));
		Auction auction = auctionRepository.save(AuctionFixture.auction(category));
		Bidding bidding = biddingRepository.save(BiddingFixture.bidding(auction, bidder, TradingStatus.PREPARING));

		chatRoom = chatRoomRepository.save(
			ChatRoomFixture.chatRoom(auction.getId(), seller, bidder, bidding.getId()));
	}

	@DisplayName("[수신받은 메시지를 벌크 연산으로 읽음처리할 수 있다.]")
	@Test
	void readReceiveMessages() {
		//given
		chatMessageRepository.saveAll(
			List.of(
				ChatMessageFixture.chatMessage(chatRoom, seller),
				ChatMessageFixture.chatMessage(chatRoom, seller)
			)
		);

		//when
		chatMessageRepository.readReceivedMessages(chatRoom, bidder.getId());

		//then
		List<ChatMessage> messages = chatMessageRepository.findAll();
		assertThat(messages.get(0).getIsRead()).isTrue();
		assertThat(messages.get(1).getIsRead()).isTrue();
	}
}