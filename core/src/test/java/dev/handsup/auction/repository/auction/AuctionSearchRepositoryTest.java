package dev.handsup.auction.repository.auction;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import dev.handsup.auction.domain.AuctionSearch;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.support.DataJpaTestSupport;
import dev.handsup.fixture.AuctionSearchFixture;
import dev.handsup.fixture.ProductFixture;
import jakarta.persistence.EntityManager;

class AuctionSearchRepositoryTest extends DataJpaTestSupport {
	private final String DIGITAL_DEVICE = "디지털 기기";
	private final String APPLIANCE = "가전제품";

	private final String KEYWORD = "버즈";
	private final PageRequest pageRequest = PageRequest.of(0, 10);
	private ProductCategory category1;
	private ProductCategory category2;
	@Autowired
	private AuctionSearchRepository auctionSearchRepository;

	@Autowired
	private EntityManager em;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@BeforeEach
	void setUp() {
		category1 = ProductFixture.productCategory(DIGITAL_DEVICE);
		category2 = ProductFixture.productCategory(APPLIANCE);
		productCategoryRepository.saveAll(List.of(category1, category2));
	}

	@DisplayName("[최근 입찰가로 필터링할 수 있다.]")
	@Test
	void searchAuction_currentBiddingPrice_min_filter() {
		//given

		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L, 1L, 2000);
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L, 2L,  5000);
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L, 3L,  10000);
		AuctionSearch auctionSearch4 = AuctionSearchFixture.auctionSearch(4L, 4L,  12000);

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3,auctionSearch4));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.minPrice(5000)
			.maxPrice(10000)
			.build();

		//when
		List<AuctionSearch> auctionSearches = auctionSearchRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctionSearches).hasSize(2),
			() -> assertThat(auctionSearches).containsExactly(auctionSearch3, auctionSearch2)
		);
	}


	@DisplayName("[경매 상품 미개봉 여부로 경매를 필터링할 수 있다. (isNewProductEq)]")
	@Test
	void searchAuction_isNewProduct_filter() {
		//given
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L, 1L, true);
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L, 2L,  false);
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L, 3L,  true);

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.isNewProduct(true)
			.build();

		//when
		List<AuctionSearch> auctionSearches = auctionSearchRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctionSearches).hasSize(2),
			() -> assertThat(auctionSearches).containsExactly(auctionSearch3, auctionSearch1)
		);
	}

	@DisplayName("[진행 중인 경매만 필터링할 수 있다. (isProgressEq)]")
	@Test
	void searchAuction_isProgress_filter() {
		//given
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L, 1L);
		ReflectionTestUtils.setField(auctionSearch1, "isProgress", false);
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L, 2L);
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L, 3L);

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.isProgress(true)
			.build();

		//when
		List<AuctionSearch> auctionSearches = auctionSearchRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctionSearches).hasSize(2),
			() -> assertThat(auctionSearches).containsExactly(auctionSearch3, auctionSearch2)
		);
	}

	@DisplayName("[거래 방식으로 경매를 필터링할 수 있다. (tradeMethodEq)]")
	@Test
	void searchAuction_tradeMethod_filter() {
		//given
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L, 1L, TradeMethod.DIRECT);
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L, 2L,  TradeMethod.DELIVER);
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L, 3L,  TradeMethod.DELIVER);

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.tradeMethod("직거래")
			.build();

		//when
		List<AuctionSearch> auctionSearches = auctionSearchRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctionSearches).hasSize(1),
			() -> assertThat(auctionSearches.get(0)).isEqualTo(auctionSearch1)
		);
	}

	@DisplayName("[검색 키워드로 필터링할 수 있다. (keywordContains)]")
	@Test
	void searchAuction_keyword_filter() {
		//given
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L,1L, KEYWORD+"팔까요?");
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L,2L, "버증팔아요");
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L,3L, KEYWORD+"팔아요");
		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.build();
		//when
		List<AuctionSearch> auctionSearches = auctionSearchRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctionSearches).hasSize(2),
			() -> assertThat(auctionSearches).containsExactly(auctionSearch3, auctionSearch1)
		);
	}
}
