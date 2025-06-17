package dev.handsup.search.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import dev.handsup.auction.domain.auction_field.TradingLocation;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionSearchRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.auction.repository.search.RedisSearchRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionSearchFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.search.domain.AuctionSearch;
import dev.handsup.search.dto.AuctionSearchCondition;

@DisplayName("[검색 API 통합 테스트]")
class SearchApiControllerTest extends ApiTestSupport {

	private final String DIGITAL_DEVICE = "디지털 기기";
	private final String KEYWORD = "버즈";

	private ProductCategory productCategory;
	@Autowired
	private AuctionSearchRepository auctionSearchRepository;
	@Autowired
	private RedisSearchRepository redisSearchRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@BeforeEach
	void setUp() {
		productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(productCategory);
	}

	@AfterEach
	public void clear() {
		Set<String> keys = redisTemplate.keys("search*");
		assert keys != null;
		redisTemplate.delete(keys);
	}

	@DisplayName("[경매를 검색해서 조회할 수 있다. 정렬 조건이 없을 경우 최신순으로 정렬한다.]")
	@Test
	void searchAuction() throws Exception {
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L,1L, KEYWORD+"팔까요?");
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L,2L, "버증팔아요");
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L,3L, KEYWORD+"팔아요");

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.build();

		mockMvc.perform(post("/api/v2/auctions/search")
				.contentType(APPLICATION_JSON)
				.content(toJson(condition)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].title").value(auctionSearch3.getTitle()))
			.andExpect(jsonPath("$.content[0].currentBiddingPrice").value(auctionSearch3.getCurrentBiddingPrice()))
			.andExpect(jsonPath("$.content[0].bookmarkCount").value(auctionSearch3.getBookmarkCount()))
			.andExpect(jsonPath("$.content[0].dong").value(auctionSearch3.getTradingLocation().getDong()))
			.andExpect(jsonPath("$.content[0].createdAt").exists())
			.andExpect(jsonPath("$.content[1].title").value(auctionSearch1.getTitle()));
	}

	@DisplayName("[경매를 지역으로 필터링하고, 북마크 순으로 정렬할 수 있다.]")
	@Test
	void searchAuctionSort() throws Exception {
		String si = "서울시", gu = "서초구", dong = "방배동", trash = "반포동";
		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L, 1L, TradingLocation.of(si,gu,dong));
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L, 2L,TradingLocation.of(si,gu,trash));
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L, 3L,TradingLocation.of(si,gu,dong));

		int bookmarkCnt = 0;
		ReflectionTestUtils.setField(auctionSearch1, "bookmarkCount", bookmarkCnt);
		ReflectionTestUtils.setField(auctionSearch2, "bookmarkCount", bookmarkCnt+1);
		ReflectionTestUtils.setField(auctionSearch3, "bookmarkCount", bookmarkCnt+2);

		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3));

		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.si(si)
			.gu(gu)
			.dong(dong)
			.keyword(KEYWORD).build();

		mockMvc.perform(post("/api/v2/auctions/search")
				.content(toJson(condition))
				.contentType(APPLICATION_JSON)
				.param("sort", "북마크수"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(2))
			.andExpect(jsonPath("$.content[0].auctionId").value(auctionSearch3.getAuctionId()))
			.andExpect(jsonPath("$.content[1].auctionId").value(auctionSearch1.getAuctionId()));
	}

	@DisplayName("[최근 입찰 가격으로 필터링할 수 있다.]")
	@Test
	void searchAuctionPriceFilter() throws Exception {
		int minRange = 10000, maxRange = 20000;

		AuctionSearch auctionSearch1 = AuctionSearchFixture.auctionSearch(1L, 1L, minRange);
		AuctionSearch auctionSearch2 = AuctionSearchFixture.auctionSearch(2L, 2L, (maxRange));
		AuctionSearch auctionSearch3 = AuctionSearchFixture.auctionSearch(3L, 3L, (minRange+maxRange)/2);
		AuctionSearch auctionSearch4 = AuctionSearchFixture.auctionSearch(4L, 4L, maxRange*2);


		auctionSearchRepository.saveAll(List.of(auctionSearch1, auctionSearch2, auctionSearch3,auctionSearch4));
		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword(KEYWORD)
			.minPrice(minRange)
			.maxPrice(maxRange)
			.build();

		mockMvc.perform(post("/api/v2/auctions/search")
				.content(toJson(condition))
				.contentType(APPLICATION_JSON)
			.param("sort", ""))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(3))
			.andExpect(jsonPath("$.content[0].auctionId").value(auctionSearch3.getId()))
			.andExpect(jsonPath("$.content[1].auctionId").value(auctionSearch2.getId()))
			.andExpect(jsonPath("$.content[2].auctionId").value(auctionSearch1.getId()));
	}

	@DisplayName("[인기 검색어 순으로 조회할 수 있다.]")
	@Test
	void getPopularKeywords() throws Exception {
		final String KEYWORD1 = "검색어1", KEYWORD2 = "검색어2", KEYWORD3 = "검색어3";
		final int KEYWORD1_COUNT = 1, KEYWORD2_COUNT = 5, KEYWORD3_COUNT = 3;
		redisSearchRepository.increaseSearchCount(KEYWORD1, KEYWORD1_COUNT);
		redisSearchRepository.increaseSearchCount(KEYWORD2, KEYWORD2_COUNT);
		redisSearchRepository.increaseSearchCount(KEYWORD3, KEYWORD3_COUNT);

		mockMvc.perform(get("/api/auctions/search/popular"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.keywords[0].keyword").value(KEYWORD2))
			.andExpect(jsonPath("$.keywords[0].count").value(KEYWORD2_COUNT))
			.andExpect(jsonPath("$.keywords[1].keyword").value(KEYWORD3))
			.andExpect(jsonPath("$.keywords[1].count").value(KEYWORD3_COUNT))
			.andExpect(jsonPath("$.keywords[2].keyword").value(KEYWORD1))
			.andExpect(jsonPath("$.keywords[2].count").value(KEYWORD1_COUNT));
	}

	@DisplayName("[인기 검색어 조회 결과가 없으면, 빈 리스트를 반환한다.]")
	@Test
	void getPopularKeywords_empty() throws Exception {
		mockMvc.perform(get("/api/auctions/search/popular"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.keywords").isEmpty());
	}

}