package dev.handsup.search.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;

class SearchApiControllerTest extends ApiTestSupport {
	private final String DIGITAL_DEVICE = "디지털 기기";
	private ProductCategory productCategory;

	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@BeforeEach
	void setUp() {
		productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(productCategory);
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
			.andExpect(jsonPath("$.content[0].description").value(auction1.getProduct().getDescription()))
			.andExpect(jsonPath("$.content[0].productStatus").value(auction1.getProduct().getStatus().getLabel()))
			.andExpect(jsonPath("$.content[0].tradeMethod").value(auction1.getTradeMethod().getLabel()))
			.andExpect(jsonPath("$.content[0].endDate").value(auction1.getEndDate().toString()))
			.andExpect(jsonPath("$.content[0].initPrice").value(auction1.getInitPrice()))
			.andExpect(jsonPath("$.content[0].purchaseTime").value(auction1.getProduct().getPurchaseTime().getLabel()))
			.andExpect(jsonPath("$.content[0].productCategory").value(
				auction1.getProduct().getProductCategory().getCategoryValue()))
			.andExpect(jsonPath("$.content[0].si").value(auction1.getTradingLocation().getSi()))
			.andExpect(jsonPath("$.content[0].gu").value(auction1.getTradingLocation().getGu()))
			.andExpect(jsonPath("$.content[0].dong").value(auction1.getTradingLocation().getDong()))
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
			.keyword(null).build();

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

}