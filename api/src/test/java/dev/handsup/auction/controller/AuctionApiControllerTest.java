package dev.handsup.auction.controller;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;

class AuctionApiControllerTest extends ApiTestSupport {

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

	@DisplayName("[경매를 등록할 수 있다.]")
	@Test
	void registerAuction() throws Exception {
		RegisterAuctionRequest request = RegisterAuctionRequest.of(
			"거의 새상품 버즈 팔아요",
			DIGITAL_DEVICE,
			10000,
			LocalDate.parse("2022-10-18"),
			ProductStatus.NEW.getLabel(),
			PurchaseTime.UNDER_ONE_MONTH.getLabel(),
			"거의 새상품이에요",
			TradeMethod.DELIVER.getLabel()
		);

		mockMvc.perform(post("/api/auctions")
				.contentType(APPLICATION_JSON)
				.content(toJson(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value(request.title()))
			.andExpect(jsonPath("$.description").value(request.description()))
			.andExpect(jsonPath("$.productStatus").value(request.productStatus()))
			.andExpect(jsonPath("$.tradeMethod").value(request.tradeMethod()))
			.andExpect(jsonPath("$.endDate").value(request.endDate().toString()))
			.andExpect(jsonPath("$.initPrice").value(request.initPrice()))
			.andExpect(jsonPath("$.purchaseTime").value(request.purchaseTime()))
			.andExpect(jsonPath("$.productCategory").value(request.productCategory()))
			.andExpect(jsonPath("$.si").isEmpty())
			.andExpect(jsonPath("$.gu").isEmpty())
			.andExpect(jsonPath("$.dong").isEmpty());
	}

	@DisplayName("[경매를 등록 시 상품 카테고리가 DB에 없으면 예외가 발생한다.]")
	@Test
	void registerAuctionFails() throws Exception {
		final String NOT_EXIST_CATEGORY = "아";
		RegisterAuctionRequest request = RegisterAuctionRequest.of(
			"거의 새상품 버즈 팔아요",
			NOT_EXIST_CATEGORY,
			10000,
			LocalDate.parse("2022-10-18"),
			ProductStatus.NEW.getLabel(),
			PurchaseTime.UNDER_ONE_MONTH.getLabel(),
			"거의 새상품이에요",
			TradeMethod.DELIVER.getLabel(),
			"서울시",
			"성북구",
			"동선동"
		);

		mockMvc.perform(post("/api/auctions")
				.contentType(APPLICATION_JSON)
				.content(toJson(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message")
				.value(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY.getMessage()))
			.andExpect(jsonPath("$.code")
				.value(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY.getCode()));
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
			.andExpect(jsonPath("$.content[0].createdDate").value(auction1.getCreatedAt().toLocalDate().toString()))
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