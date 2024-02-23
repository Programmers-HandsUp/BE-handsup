package dev.handsup.bidding.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.support.DataJpaTestSupport;
import dev.handsup.user.domain.User;

@DisplayName("[BiddingRepository 테스트]")
class BiddingRepositoryTest extends DataJpaTestSupport {

	@Autowired
	private BiddingRepository biddingRepository;

	private Auction auction = AuctionFixture.auction();
	private User bidder = UserFixture.user();

	@BeforeEach
	void setUp() {
		Bidding biddingA = Bidding.of(10000, auction, bidder);
		em.persist(biddingA);
		Bidding biddingB = Bidding.of(20000, auction, bidder);
		em.persist(biddingB);
		Bidding biddingC = Bidding.of(30000, auction, bidder);
		em.persist(biddingC);

		em.flush();
	}

	@Test
	@DisplayName("[경매 id로 조회된 경매에서 최고 입찰가를 찾을 수 있다]")
	void findMaxBiddingPriceByAuctionId() {
		// When
		Integer maxPrice = biddingRepository.findMaxBiddingPriceByAuctionId(auction.getId());

		// Then
		assertThat(maxPrice).isEqualTo(30000);
	}
}
