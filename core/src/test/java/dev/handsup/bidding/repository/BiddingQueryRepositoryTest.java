package dev.handsup.bidding.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

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
import jakarta.persistence.EntityManager;

@Import(TestAuditingConfig.class)
@DisplayName("[BiddingQueryRepository 테스트]")
class BiddingQueryRepositoryTest extends DataJpaTestSupport {

	private Auction auction1, auction2, auction3;
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

	@Autowired
	private EntityManager em;

	@BeforeEach
	void setUp() {
		ProductCategory category = productCategoryRepository.save(ProductFixture.productCategory("디지털 기기"));
		auction1 = auctionRepository.save(AuctionFixture.auction(category));
		auction2 = auctionRepository.save(AuctionFixture.auction(category));
		auction3 = auctionRepository.save(AuctionFixture.auction(category));
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
		assertThat(actualBidding.getId()).isEqualTo(waitingBidding2.getId());
	}

	@Test
	@DisplayName("[경매 종료일이 하루 지난 각 경매들에서 최신 입찰을 조회해 상태를 업데이트한다.]")
	void updateBiddingTradingStatus() {
		//given
		ReflectionTestUtils.setField(auction1, "endDate", LocalDate.now().minusDays(1));
		ReflectionTestUtils.setField(auction2, "endDate", LocalDate.now().minusDays(1));
		ReflectionTestUtils.setField(auction3, "endDate", LocalDate.now());

		Bidding auction1Bidding1 = BiddingFixture.bidding(bidder, auction1, TradingStatus.WAITING);
		Bidding auction1Bidding2 = BiddingFixture.bidding(bidder, auction1, TradingStatus.WAITING);
		Bidding auction2Bidding1 = BiddingFixture.bidding(bidder, auction2, TradingStatus.WAITING);
		Bidding auction2Bidding2 = BiddingFixture.bidding(bidder, auction2, TradingStatus.WAITING);
		Bidding auction3Bidding = BiddingFixture.bidding(bidder, auction3, TradingStatus.WAITING);

		biddingRepository.saveAll(
			List.of(auction1Bidding1, auction1Bidding2, auction2Bidding1, auction2Bidding2, auction3Bidding));

		//when
		biddingQueryRepository.updateBiddingTradingStatus();

		em.flush();
		em.clear();
		Bidding foundAuction1Bidding1 = biddingRepository.findById(auction1Bidding1.getId()).orElseThrow();
		Bidding foundAuction1Bidding2 = biddingRepository.findById(auction1Bidding2.getId()).orElseThrow();
		Bidding foundAuction2Bidding1 = biddingRepository.findById(auction2Bidding1.getId()).orElseThrow();
		Bidding foundAuction2Bidding2 = biddingRepository.findById(auction2Bidding2.getId()).orElseThrow();
		Bidding foundAuction3Bidding = biddingRepository.findById(auction3Bidding.getId()).orElseThrow();

		//then
		assertThat(foundAuction1Bidding1.getTradingStatus()).isEqualTo(TradingStatus.WAITING);
		assertThat(foundAuction1Bidding2.getTradingStatus()).isEqualTo(TradingStatus.PREPARING);
		assertThat(foundAuction2Bidding1.getTradingStatus()).isEqualTo(TradingStatus.WAITING);
		assertThat(foundAuction2Bidding2.getTradingStatus()).isEqualTo(TradingStatus.PREPARING);
		assertThat(foundAuction3Bidding.getTradingStatus()).isEqualTo(TradingStatus.WAITING);
	}
}