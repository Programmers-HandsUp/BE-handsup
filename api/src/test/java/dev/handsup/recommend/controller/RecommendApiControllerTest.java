package dev.handsup.recommend.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import dev.handsup.auction.domain.auction_field.TradingLocation;
import dev.handsup.auction.domain.product.product_category.PreferredProductCategory;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionSearchRepository;
import dev.handsup.auction.repository.product.PreferredProductCategoryRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionSearchFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.search.domain.AuctionSearch;

@DisplayName("[Auction 통합 테스트]")
class RecommendApiControllerTest extends ApiTestSupport {

	private final String DIGITAL_DEVICE = "디지털 기기";
	private ProductCategory productCategory;
	@Autowired
	private AuctionSearchRepository auctionSearchRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private PreferredProductCategoryRepository preferredProductCategoryRepository;

	@BeforeEach
	void setUp() {
		productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(productCategory);
		userRepository.save(user);
	}

	@DisplayName("[지역 필터에 따라 경매글 목록을 반환한다.]")
	@Test
	void getRecommendAuctionsWithFilter() throws Exception {
		//given
		String si = "서울시", gu = "서초구", dong = "방배동", trash = "반포동";
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L,1L, TradingLocation.of(si,gu,dong));
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L,2L, TradingLocation.of(si,gu,trash));
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L,3L, TradingLocation.of(si,gu,dong));

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));

		//when
		mockMvc.perform(get("/api/v2/auctions/recommend").param("sort", "최신순")
				.param("si", si)
				.param("gu", gu)
				.param("dong", dong)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(2))
			.andExpect(jsonPath("$.content[0].auctionId").value(auctionSearch3.getAuctionId()))
			.andExpect(jsonPath("$.content[1].auctionId").value(auctionSearch1.getAuctionId()))
			.andExpect(jsonPath("$.hasNext").value(false));
	}

	@DisplayName("[정렬 조건과 지역 필터에 따라 경매글 목록을 반환한다.]")
	@Test
	void getRecommendAuctionsWithFilterAndSort() throws Exception {
		//given
		String si = "서울시", gu = "서초구", dong = "방배동", trash = "반포동";

		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L,1L,TradingLocation.of(si,gu,dong),
			LocalDate.now().minusDays(1));
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L,2L,TradingLocation.of(si,gu,trash),
			LocalDate.now());
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L,3L,TradingLocation.of(si,gu,dong),
			LocalDate.now().plusDays(1));
		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));

		//when
		mockMvc.perform(get("/api/v2/auctions/recommend").param("sort", "마감일")
				.param("si", si)
				.param("gu", gu)
				.param("dong", dong)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(2))
			.andExpect(jsonPath("$.content[0].auctionId").value(auctionSearch1.getAuctionId()))
			.andExpect(jsonPath("$.content[0].endDate").value(auctionSearch1.getEndDate().atStartOfDay().toString()))
			.andExpect(jsonPath("$.content[1].auctionId").value(auctionSearch3.getAuctionId()))
			.andExpect(jsonPath("$.content[1].endDate").value(auctionSearch3.getEndDate().atStartOfDay().toString()))
			.andExpect(jsonPath("$.hasNext").value(false));
	}

	@DisplayName("[정렬 조건이 없으면 최신순으로 반환한다.]")
	@Test
	void getRecommendAuctionsWithOutSort() throws Exception {
		//given
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L,1L);
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L,2L);

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2));

		//when
		mockMvc.perform(get("/api/v2/auctions/recommend").contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(2))
			.andExpect(jsonPath("$.content[0].auctionId").value(auctionSearch2.getAuctionId()))
			.andExpect(jsonPath("$.content[1].auctionId").value(auctionSearch1.getAuctionId()))
			.andExpect(jsonPath("$.hasNext").value(false));
	}


	@DisplayName("[유저 선호 카테고리 경매를 북마크 순으로 정렬한다.]")
	@Test
	void getUserPreferredCategoryAuctions() throws Exception {
		ProductCategory productCategory2 = productCategoryRepository.save(ProductCategory.from("생활/주방"));
		ProductCategory notPreferredProductCategory = productCategoryRepository.save(ProductCategory.from("티켓/교환권"));

		preferredProductCategoryRepository.saveAll(List.of(
			PreferredProductCategory.of(user, productCategory),
			PreferredProductCategory.of(user, productCategory2)
		));

		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L,productCategory.getValue(),1L);
		ReflectionTestUtils.setField(auctionSearch1, "bookmarkCount", 3);
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L,productCategory.getValue(),2L);
		ReflectionTestUtils.setField(auctionSearch2, "bookmarkCount", 5);
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L,notPreferredProductCategory.getValue(),3L);

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));

		//when
		mockMvc.perform(get("/api/v2/auctions/recommend/category")
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(2))
			.andExpect(jsonPath("$.content[0].auctionId").value(auctionSearch2.getId()))
			.andExpect(jsonPath("$.content[1].auctionId").value(auctionSearch1.getId()))
			.andExpect(jsonPath("$.hasNext").value(false));
	}
}

