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

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.auction.repository.search.RedisSearchRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;

@DisplayName("[검색 API 통합 테스트]")
class SearchApiControllerTest extends ApiTestSupport {

	private final String DIGITAL_DEVICE = "디지털 기기";
	private ProductCategory productCategory;

	@Autowired
	private AuctionRepository auctionRepository;

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
		Auction auction1 = AuctionFixture.auction(productCategory, "버즈 이어폰 팔아요");
		Auction auction2 = AuctionFixture.auction(productCategory, "에버어즈팟");
		Auction auction3 = AuctionFixture.auction(productCategory, "버즈 이어폰 팔아요");
		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword("버즈").build();
		auctionRepository.saveAll(List.of(auction1, auction2, auction3));

		mockMvc.perform(post("/api/auctions/search")
				.contentType(APPLICATION_JSON)
				.content(toJson(condition)))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].title").value(auction1.getTitle()))
			.andExpect(jsonPath("$.content[0].initPrice").value(auction1.getInitPrice()))
			.andExpect(jsonPath("$.content[0].bookmarkCount").value(auction1.getBookmarkCount()))
			.andExpect(jsonPath("$.content[0].dong").value(auction1.getTradingLocation().getDong()))
			.andExpect(jsonPath("$.content[0].createdAt").exists())
			.andExpect(jsonPath("$.content[1].title").value(auction3.getTitle()));
	}

	@DisplayName("[경매를 북마크 순으로 정렬할 수 있다.]")
	@Test
	void searchAuctionSort() throws Exception {
		Auction auction1 = AuctionFixture.auction(productCategory);
		Auction auction2 = AuctionFixture.auction(productCategory);
		Auction auction3 = AuctionFixture.auction(productCategory);
		ReflectionTestUtils.setField(auction2, "bookmarkCount", 5);
		ReflectionTestUtils.setField(auction3, "bookmarkCount", 3);
		auctionRepository.saveAll(List.of(auction1, auction2, auction3));
		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword("버즈").build();

		mockMvc.perform(post("/api/auctions/search")
				.content(toJson(condition))
				.contentType(APPLICATION_JSON)
				.param("sort", "bookmarkCount,desc"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(3))
			.andExpect(jsonPath("$.content[0].auctionId").value(auction2.getId()))
			.andExpect(jsonPath("$.content[1].auctionId").value(auction3.getId()))
			.andExpect(jsonPath("$.content[2].auctionId").value(auction1.getId()));
	}

	@DisplayName("[검색된 경매를 필터링할 수 있다.]")
	@Test
	void searchAuctionFilter() throws Exception {
		Auction auction1 = AuctionFixture.auction(productCategory, "버즈", 15000);
		Auction auction2 = AuctionFixture.auction(productCategory, "에어팟", 15000);
		Auction auction3 = AuctionFixture.auction(productCategory, "버즈 팔아요", 25000);
		ReflectionTestUtils.setField(auction2, "bookmarkCount", 5);
		ReflectionTestUtils.setField(auction3, "bookmarkCount", 3);
		auctionRepository.saveAll(List.of(auction1, auction2, auction3));
		AuctionSearchCondition condition = AuctionSearchCondition.builder()
			.keyword("버즈")
			.minPrice(10000)
			.maxPrice(20000)
			.build();

		mockMvc.perform(post("/api/auctions/search")
				.content(toJson(condition))
				.contentType(APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(1))
			.andExpect(jsonPath("$.content[0].auctionId").value(auction1.getId()));
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