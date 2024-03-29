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
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.support.DataJpaTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;
import jakarta.persistence.EntityManager;

@DisplayName("[AuctionQueryRepositoryImpl 테스트]")
class AuctionQueryRepositoryImplTest extends DataJpaTestSupport {

	private final String DIGITAL_DEVICE = "디지털 기기";
	private final String APPLIANCE = "가전제품";

	private final String KEYWORD = "버즈";
	private final PageRequest pageRequest = PageRequest.of(0, 10);
	private ProductCategory category1;
	private ProductCategory category2;
	@Autowired
	private AuctionQueryRepository auctionQueryRepository;

	@Autowired
	private EntityManager em;

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@BeforeEach
	void setUp() {
		category1 = ProductFixture.productCategory(DIGITAL_DEVICE);
		category2 = ProductFixture.productCategory(APPLIANCE);
		productCategoryRepository.saveAll(List.of(category1, category2));
	}

	@DisplayName("[경매 시작 금액에 min값을 설정해 필터링할 수 있다.(minGoe)]")
	@Test
	void searchAuction_initPrice_min_filter() {
		//given
		Auction auction1 = AuctionFixture.auction(category1, 2000);
		Auction auction2 = AuctionFixture.auction(category2, 5000);
		Auction auction3 = AuctionFixture.auction(category2, 10000);

		auctionRepository.saveAll(List.of(auction1, auction2, auction3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.minPrice(5000)
			.build();

		//when
		List<Auction> auctions = auctionQueryRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctions).hasSize(2),
			() -> assertThat(auctions).containsExactly(auction2, auction3)
		);
	}

	@DisplayName("[경매 시작 금액에 max값을 설정해 필터링할 수 있다.(maxLoe)]")
	@Test
	void searchAuction_initPrice_max_filter() {
		//given
		Auction auction1 = AuctionFixture.auction(category1, 2000);
		Auction auction2 = AuctionFixture.auction(category2, 5000);
		Auction auction3 = AuctionFixture.auction(category2, 10000);

		auctionRepository.saveAll(List.of(auction1, auction2, auction3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.maxPrice(5000)
			.build();

		//when
		List<Auction> auctions = auctionQueryRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctions).hasSize(2),
			() -> assertThat(auctions).containsExactly(auction1, auction2)
		);
	}

	@DisplayName("[경매 상품 미개봉 여부로 경매를 필터링할 수 있다. (isNewProductEq)]")
	@Test
	void searchAuction_isNewProduct_filter() {
		//given
		Auction auction1 = AuctionFixture.auction(category1, ProductStatus.NEW);
		Auction auction2 = AuctionFixture.auction(category2, ProductStatus.DIRTY);
		Auction auction3 = AuctionFixture.auction(category2, ProductStatus.CLEAN);
		auctionRepository.saveAll(List.of(auction1, auction2, auction3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.isNewProduct(false)
			.build();

		//when
		List<Auction> auctions = auctionQueryRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctions).hasSize(2),
			() -> assertThat(auctions).containsExactly(auction2, auction3)
		);
	}

	@DisplayName("[진행 중인 경매만 필터링할 수 있다. (isProgressEq)]")
	@Test
	void searchAuction_isProgress_filter() {
		//given
		Auction auction1 = AuctionFixture.auction(category1);
		Auction auction2 = AuctionFixture.auction(category2);
		Auction auction3 = AuctionFixture.auction(category2);
		auction1.updateAuctionStatusTrading();
		auctionRepository.saveAll(List.of(auction1, auction2, auction3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.isProgress(true)
			.build();

		//when
		List<Auction> auctions = auctionQueryRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctions).hasSize(2),
			() -> assertThat(auctions).containsExactly(auction2, auction3)
		);
	}

	@DisplayName("[거래 방식으로 경매를 필터링할 수 있다. (tradeMethodEq)]")
	@Test
	void searchAuction_tradeMethod_filter() {
		//given
		Auction auction1 = AuctionFixture.auction(category1, TradeMethod.DELIVER);
		Auction auction2 = AuctionFixture.auction(category2, TradeMethod.DIRECT);
		auctionRepository.saveAll(List.of(auction1, auction2));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.tradeMethod("택배")
			.build();

		//when
		List<Auction> auctions = auctionQueryRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctions).hasSize(1),
			() -> assertThat(auctions.get(0)).isEqualTo(auction1)
		);
	}

	@DisplayName("[검색 키워드로 필터링할 수 있다. (keywordContains)]")
	@Test
	void searchAuction_keyword_filter() {
		//given
		Auction auction1 = AuctionFixture.auction(category1, "버즈팔아요");
		Auction auction2 = AuctionFixture.auction(category1, "버증팔아요");
		Auction auction3 = AuctionFixture.auction(category2, "버즈팔아요");
		auctionRepository.saveAll(List.of(auction1, auction2, auction3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.productCategory(DIGITAL_DEVICE)
			.build();
		//when
		List<Auction> auctions = auctionQueryRepository.searchAuctions(condition, pageRequest).getContent();

		//then
		assertAll(
			() -> assertThat(auctions).hasSize(1),
			() -> assertThat(auctions.get(0)).isEqualTo(auction1)
		);
	}

	@DisplayName("[다음 슬라이스에 요소가 있으면 hasNext()=true]")
	@Test
	void searchAuction_hasNext() {
		//given
		Auction auction1 = AuctionFixture.auction(category1);
		Auction auction2 = AuctionFixture.auction(category1);
		auctionRepository.saveAll(List.of(auction1, auction2));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.si("서울시")
			.productCategory(DIGITAL_DEVICE)
			.build();
		PageRequest pageRequest = PageRequest.of(0, 1);

		//when
		Slice<Auction> auctions = auctionQueryRepository.searchAuctions(condition, pageRequest);

		//then
		assertThat(auctions.hasNext()).isTrue();
	}

	@DisplayName("[입찰수 순으로 경매를 조회할 수 있다.]")
	@Test
	void sortAuctionByCriteria() {
		//given
		Auction auction1 = AuctionFixture.auction(category1);
		ReflectionTestUtils.setField(auction1, "biddingCount", 4);
		Auction auction2 = AuctionFixture.auction(category1);
		ReflectionTestUtils.setField(auction2, "biddingCount", 5);

		auctionRepository.saveAll(List.of(auction1, auction2));
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("입찰수"));
		//when
		List<Auction> auctions = auctionQueryRepository.sortAuctionByCriteria(null, null, null, pageRequest)
			.getContent();
		//then
		assertThat(auctions).containsExactly(auction2, auction1);
	}

	@DisplayName("[특정 지역 필터 + 북마크순으로 경매를 조회할 수 있다.]")
	@Test
	void sortAuctionByCriteria2() {
		//given
		String si = "서울시", gu = "서초구", dong1 = "방배동", dong2 = "반포동";
		Auction auction1 = AuctionFixture.auction(category1, si, gu, dong1);
		ReflectionTestUtils.setField(auction1, "bookmarkCount", 4);
		Auction auction2 = AuctionFixture.auction(category2, si, gu, dong1);
		ReflectionTestUtils.setField(auction2, "bookmarkCount", 5);
		Auction auction3 = AuctionFixture.auction(category2, si, gu, dong2);
		ReflectionTestUtils.setField(auction2, "bookmarkCount", 5);

		auctionRepository.saveAll(List.of(auction1, auction2, auction3));
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("북마크수"));

		//when
		List<Auction> auctions = auctionQueryRepository.sortAuctionByCriteria(si, gu, dong1, pageRequest)
			.getContent();
		//then
		assertThat(auctions).containsExactly(auction2, auction1);
	}

	@DisplayName("[사용자 선호 카테고리에 속하는 해당하는 경매를 북마크순으로 조회할 수 있다.]")
	@Test
	void findByProductCategories() {
		//given
		ProductCategory notPreferredCategory = productCategoryRepository.save(ProductCategory.from("스포츠/레저"));
		Auction auction1 = AuctionFixture.auction(category1);
		ReflectionTestUtils.setField(auction1, "bookmarkCount", 4);
		Auction auction2 = AuctionFixture.auction(category2);
		ReflectionTestUtils.setField(auction2, "bookmarkCount", 5);
		Auction auction3 = auctionRepository.save(AuctionFixture.auction(notPreferredCategory));
		ReflectionTestUtils.setField(auction3, "bookmarkCount", 10);
		auctionRepository.saveAll(List.of(auction1, auction2, auction3));

		//when
		List<Auction> auctions = auctionQueryRepository.findByProductCategories(
			List.of(category1, category2), pageRequest).getContent();
		//then
		assertThat(auctions).containsExactly(auction2, auction1);

	}

	@DisplayName("[마감 일자가 지난 경매의 상태를 새로운 경매 상태로 변경한다.")
	@Test
	void updateAuctionStatus() {
		//given
		LocalDate today = LocalDate.now();

		Auction endedAuctionWithBidder = AuctionFixture.auction(category1, today.minusDays(1)); // 마감 일자(endDate)
		Auction endedAuctionWithOutBidder = AuctionFixture.auction(category1, today.minusDays(1)); // 마감 일자(endDate)
		Auction biddingAuction1 = AuctionFixture.auction(category1, today);
		Auction biddingAuction2 = AuctionFixture.auction(category1, today.plusDays(1));

		ReflectionTestUtils.setField(endedAuctionWithBidder, "biddingCount", 1);
		auctionRepository.saveAll(
			List.of(endedAuctionWithBidder, endedAuctionWithOutBidder, biddingAuction1, biddingAuction2));

		//when
		//벌크 업데이트(영속성 컨텍스트 거치지 않음) 후 영속성 컨텍스트 비움
		auctionQueryRepository.updateAuctionStatusAfterEndDate();

		em.flush();
		em.clear();

		Auction savedEndedAuctionWithBidder = auctionRepository.findById(endedAuctionWithBidder.getId()).orElseThrow();
		Auction savedEndedAuctionWithOutBidder = auctionRepository.findById(endedAuctionWithOutBidder.getId())
			.orElseThrow();
		Auction savedBiddingAuction1 = auctionRepository.findById(biddingAuction1.getId()).orElseThrow();
		Auction savedBiddingAuction2 = auctionRepository.findById(biddingAuction2.getId()).orElseThrow();

		//then
		assertAll(
			() -> assertThat(savedEndedAuctionWithBidder.getStatus()).isEqualTo(AuctionStatus.TRADING),
			() -> assertThat(savedEndedAuctionWithOutBidder.getStatus()).isEqualTo(AuctionStatus.CANCELED),
			() -> assertThat(savedBiddingAuction1.getStatus()).isEqualTo(AuctionStatus.BIDDING),
			() -> assertThat(savedBiddingAuction2.getStatus()).isEqualTo(AuctionStatus.BIDDING)
		);
	}
}
