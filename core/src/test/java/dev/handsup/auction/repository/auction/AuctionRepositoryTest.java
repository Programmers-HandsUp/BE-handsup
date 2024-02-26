package dev.handsup.auction.repository.auction;

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
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bookmark.domain.Bookmark;
import dev.handsup.bookmark.repository.BookmarkRepository;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BookmarkFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.support.DataJpaTestSupport;
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
	private Auction auction1, auction2;

	@BeforeEach
	void setUp() {
		category = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(category);

		user = UserFixture.user();
		userRepository.save(user);

		auction1 = AuctionFixture.auction(category);
		auction2 = AuctionFixture.auction(category);
		auctionRepository.saveAll(List.of(auction1, auction2));

	}

	@DisplayName("[유저가 북마크한 경매를 모두 조회할 수 있다.]")
	@Test
	void findBookmarkAuction() {
		//given
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
}
