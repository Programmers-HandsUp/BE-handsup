package dev.handsup.auction.repository.auction;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.auction_field.TradingLocation;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.support.DataJpaTestSupport;
import dev.handsup.fixture.AuctionSearchFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.search.domain.AuctionSearch;
import dev.handsup.search.dto.AuctionSearchCondition;
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
		assertThat(auctionSearches).containsExactly(auctionSearch3, auctionSearch2);
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
		assertThat(auctionSearches).containsExactly(auctionSearch3, auctionSearch1);
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
		assertThat(auctionSearches).containsExactly(auctionSearch3, auctionSearch2);
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
		assertThat(auctionSearches).containsExactly(auctionSearch1);
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
		assertThat(auctionSearches).containsExactly(auctionSearch3, auctionSearch1);
	}

	@DisplayName("[입찰수 순으로 경매를 조회할 수 있다.]")
	@Test
	void sortAuctionByCriteria_biddingCount() {
		//given
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L, 1L);
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L, 2L);
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L, 3L);

		int biddingCnt = 0;
		ReflectionTestUtils.setField(auctionSearch1, "biddingCount", biddingCnt);
		ReflectionTestUtils.setField(auctionSearch2, "biddingCount", biddingCnt+1);
		ReflectionTestUtils.setField(auctionSearch3, "biddingCount", biddingCnt+2);

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2,auctionSearch3));


		PageRequest request = PageRequest.of(0, 10, Sort.by("입찰수"));
		//when
		List<AuctionSearch> auctionSearches = auctionSearchRepository.sortAuctionByCriteria(null, null, null, request)
			.getContent();
		//then
		assertThat(auctionSearches).containsExactly(auctionSearch3, auctionSearch2, auctionSearch1);
	}

	@DisplayName("[특정 지역 필터 + 북마크순으로 경매를 조회할 수 있다.]")
	@Test
	void sortAuctionByCriteria_bookmarkCount() {
		//given
		String si = "서울시", gu = "서초구", dong1 = "방배동", dong2 = "반포동";
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L, 1L, TradingLocation.of(si,gu,dong1));
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L, 2L,TradingLocation.of(si,gu,dong1));
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L, 3L,TradingLocation.of(si,gu,dong2));

		int bookmarkCnt = 0;
		ReflectionTestUtils.setField(auctionSearch1, "bookmarkCount", bookmarkCnt);
		ReflectionTestUtils.setField(auctionSearch2, "bookmarkCount", bookmarkCnt+1);
		ReflectionTestUtils.setField(auctionSearch3, "bookmarkCount", bookmarkCnt+2);

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));
		PageRequest request = PageRequest.of(0, 10, Sort.by("북마크수"));

		//when
		List<AuctionSearch> auctionSearches = auctionSearchRepository.sortAuctionByCriteria(si, gu, dong1, request)
			.getContent();
		//then
		assertThat(auctionSearches).containsExactly(auctionSearch2, auctionSearch1);
	}

	@DisplayName("[사용자 선호 카테고리에 속하는 해당하는 경매를 북마크순으로 조회할 수 있다.]")
	@Test
	void findByProductCategories() {
		//given
		int bookmarkCnt = 0;
		String notPreferredCategory = "스포츠/레저";
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L, category1.getValue(),1L);
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L, category2.getValue(),2L);
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L, notPreferredCategory,3L);

		ReflectionTestUtils.setField(auctionSearch1, "bookmarkCount", bookmarkCnt+2);
		ReflectionTestUtils.setField(auctionSearch2, "bookmarkCount", bookmarkCnt+1);
		ReflectionTestUtils.setField(auctionSearch3, "bookmarkCount", bookmarkCnt);
		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));

		//when
		List<AuctionSearch> auctionSearches = auctionSearchRepository.findByProductCategories(
			List.of(category1.getValue(), category2.getValue()), pageRequest).getContent();
		//then
		assertThat(auctionSearches).containsExactly(auctionSearch1, auctionSearch2);
	}
}
