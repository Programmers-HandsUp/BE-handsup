package dev.handsup.bidding.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.common.support.DataJpaTestSupport;
import dev.handsup.config.TestAuditingConfig;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BiddingFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;

@Import(TestAuditingConfig.class)
@DisplayName("[BiddingQueryRepository 테스트]")
class BiddingQueryRepositoryTest extends DataJpaTestSupport {

	private Auction auction1, auction2;
	private User bidder;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private BiddingQueryRepository biddingQueryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BiddingRepository biddingRepository;

	@Autowired
	private AuctionRepository auctionRepository;

	@BeforeEach
	void setUp() {
		ProductCategory category = productCategoryRepository.save(ProductFixture.productCategory("디지털 기기"));
		auction1 = auctionRepository.save(AuctionFixture.auction(category));
		auction2 = auctionRepository.save(AuctionFixture.auction(category));
		bidder = userRepository.save(UserFixture.user2());
	}

	@Test
	@DisplayName("[경매 내 대기 상태 입찰들 중 가장 최신 입찰(=가격 높은 입찰)을 조회한다.]")
	void findWaitingBiddingLatest() {
	    //given
		Bidding waitingBidding1 = BiddingFixture.bidding(bidder, auction1, TradingStatus.WAITING, 2000);
		Bidding waitingBidding2 = BiddingFixture.bidding(bidder, auction1, TradingStatus.WAITING, 3000);
		Bidding anotherAuctionBidding = BiddingFixture.bidding(bidder, auction2, TradingStatus.WAITING, 4000);
		Bidding progressingBidding = BiddingFixture.bidding(bidder, auction1, TradingStatus.PROGRESSING, 5000);
		biddingRepository.saveAll(List.of(waitingBidding1, waitingBidding2, anotherAuctionBidding, progressingBidding));

		//when
		Bidding actualBidding = biddingQueryRepository.findWaitingBiddingLatest(auction1).orElseThrow();

		//then
		Assertions.assertThat(actualBidding.getId()).isEqualTo(waitingBidding2.getId());
	}
}