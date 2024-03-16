package dev.handsup.auction.repository.auction;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bookmark.domain.Bookmark;
import dev.handsup.bookmark.repository.BookmarkRepository;
import dev.handsup.common.support.DataJpaTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BookmarkFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;

@DisplayName("[AuctionRepository 테스트]")
class AuctionRepositoryTest extends DataJpaTestSupport {

	private final String DIGITAL_DEVICE = "디지털 기기";
	private final PageRequest pageRequest = PageRequest.of(0, 10);

	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	@Autowired
	private BookmarkRepository bookmarkRepository;

	private User user;
	private ProductCategory category;

	@BeforeEach
	void setUp() {
		category = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(category);

		user = UserFixture.user1();
		userRepository.save(user);
	}

	@DisplayName("[유저가 북마크한 경매를 모두 조회할 수 있다.]")
	@Test
	void findBookmarkAuction() {
		//given
		Auction auction1 = AuctionFixture.auction(category);
		Auction auction2 = AuctionFixture.auction(category);
		auctionRepository.saveAll(List.of(auction1, auction2));

		Bookmark bookmark1 = BookmarkFixture.bookmark(user, auction1);
		Bookmark bookmark2 = BookmarkFixture.bookmark(user, auction2);
		bookmarkRepository.saveAll(List.of(bookmark1, bookmark2));

		//when
		Slice<Auction> auctionSlice = auctionRepository.findBookmarkAuction(user, pageRequest);
		List<Auction> auctions = auctionSlice.getContent();
		//then
		assertAll(
			() -> assertThat(auctionSlice).hasSize(2),
			() -> assertThat(auctionSlice.hasNext()).isFalse(),
			() -> assertThat(auctions.get(0)).isEqualTo(auction1),
			() -> assertThat(auctions.get(1)).isEqualTo(auction2)
		);

	}

	@DisplayName("[마감 일자가 지난 경매의 상태를 새로운 경매 상태로 변경한다.")
	@Test
	void updateAuctionStatus() {
		//given
		LocalDate today = LocalDate.now();
		Auction auction1 = AuctionFixture.auction(category, today.minusDays(1)); // 마감 일자(endDate)
		Auction auction2 = AuctionFixture.auction(category, today);
		Auction auction3 = AuctionFixture.auction(category, today.plusDays(1));
		auctionRepository.saveAll(List.of(auction1, auction2, auction3));

		//when
		auctionRepository.updateAuctionStatus(AuctionStatus.BIDDING, AuctionStatus.TRADING,
			today); //벌크 업데이트(영속성 컨텍스트 거치지 않음) 후 영속성 컨텍스트 비움
		Auction savedAuction1 = auctionRepository.findById(auction1.getId()).orElseThrow();
		Auction savedAuction2 = auctionRepository.findById(auction2.getId()).orElseThrow();
		Auction savedAuction3 = auctionRepository.findById(auction3.getId()).orElseThrow();

		//then
		assertAll(
			() -> assertThat(savedAuction1.getStatus()).isEqualTo(AuctionStatus.TRADING),
			() -> assertThat(savedAuction2.getStatus()).isEqualTo(AuctionStatus.BIDDING),
			() -> assertThat(savedAuction3.getStatus()).isEqualTo(AuctionStatus.BIDDING)
		);
	}
}
