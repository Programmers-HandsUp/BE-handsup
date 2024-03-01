package dev.handsup.auction.controller;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

@DisplayName("[Auction 통합 테스트]")
class AuctionApiControllerTest extends ApiTestSupport {

	private final String DIGITAL_DEVICE = "디지털 기기";
	private final User user = UserFixture.user();
	private ProductCategory productCategory;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@BeforeEach
	void setUp() {
		productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(productCategory);
		userRepository.save(user);
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
			TradeMethod.DELIVER.getLabel(),
			List.of("test.jpg")
		);

		mockMvc.perform(post("/api/auctions")
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.content(toJson(request))
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.sellerId").value(user.getId()))
			.andExpect(jsonPath("$.title").value(request.title()))
			.andExpect(jsonPath("$.description").value(request.description()))
			.andExpect(jsonPath("$.productStatus").value(request.productStatus()))
			.andExpect(jsonPath("$.tradeMethod").value(request.tradeMethod()))
			.andExpect(jsonPath("$.endDate").value(request.endDate().toString()))
			.andExpect(jsonPath("$.initPrice").value(request.initPrice()))
			.andExpect(jsonPath("$.purchaseTime").value(request.purchaseTime()))
			.andExpect(jsonPath("$.productCategory").value(request.productCategory()))
			.andExpect(jsonPath("$.imageUrls[0]").value(request.imageUrls().get(0)))
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
			List.of("test.jpg"),
			"서울시",
			"성북구",
			"동선동"
		);

		mockMvc.perform(post("/api/auctions")
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.content(toJson(request))
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message")
				.value(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY.getMessage()))
			.andExpect(jsonPath("$.code")
				.value(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY.getCode()));
	}

	@DisplayName("[경매를 상세정보를 조회할 수 있다.]")
	@Test
	void getAuctionDetail() throws Exception {
		//given
		Auction auction = AuctionFixture.auction(productCategory);
		auctionRepository.save(auction);

		//when, then
		mockMvc.perform(get("/api/auctions/{auctionId}", auction.getId())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.auctionId").value(auction.getId()))
			.andExpect(jsonPath("$.sellerId").value(user.getId()))
			.andExpect(jsonPath("$.title").value(auction.getTitle()))
			.andExpect(jsonPath("$.productCategory")
				.value(auction.getProduct().getProductCategory().getCategoryValue()))
			.andExpect(jsonPath("$.initPrice").value(auction.getInitPrice()))
			.andExpect(jsonPath("$.currentBiddingPrice").value(auction.getCurrentBiddingPrice()))
			.andExpect(jsonPath("$.endDate").value(auction.getEndDate().toString()))
			.andExpect(jsonPath("$.productStatus").value(auction.getProduct().getStatus().getLabel()))
			.andExpect(jsonPath("$.purchaseTime").value(auction.getProduct().getPurchaseTime().getLabel()))
			.andExpect(jsonPath("$.description").value(auction.getProduct().getDescription()))
			.andExpect(jsonPath("$.tradeMethod").value(auction.getTradeMethod().getLabel()))
			.andExpect(jsonPath("$.imageUrls[0]").value(auction.getProduct().getImages().get(0).getImageUrl()))
			.andExpect(jsonPath("$.si").value(auction.getTradingLocation().getSi()))
			.andExpect(jsonPath("$.gu").value(auction.getTradingLocation().getGu()))
			.andExpect(jsonPath("$.dong").value(auction.getTradingLocation().getDong()))
			.andExpect(jsonPath("$.bookmarkCount").value(auction.getBookmarkCount()));
	}

	@DisplayName("[정렬 조건과 지역 필터에 따라 경매글 목록을 반환한다.]")
	@Test
	void getRecommendAuctionsWithFilter() throws Exception {
		//given
		String si = "서울시", gu = "서초구", dong1 = "방배동", dong2 = "반포동";
		Auction auction1 = AuctionFixture.auction(productCategory, "2024-03-11", si, gu, dong1);
		Auction auction2 = AuctionFixture.auction(productCategory, "2024-03-07", si, gu, dong1);
		Auction auction3 = AuctionFixture.auction(productCategory, "2024-03-08", si, gu, dong2);
		auctionRepository.saveAll(List.of(auction1, auction2, auction3));

		//when
		mockMvc.perform(get("/api/auctions/recommend")
				.param("sort", "마감일")
				.param("si", si)
				.param("gu", gu)
				.param("dong", dong1)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(2))
			.andExpect(jsonPath("$.content[0].auctionId").value(auction2.getId()))
			.andExpect(jsonPath("$.content[0].createdAt").value(auction2.getCreatedAt().toString()))
			.andExpect(jsonPath("$.content[1].auctionId").value(auction1.getId()))
			.andExpect(jsonPath("$.content[1].createdAt").value(auction1.getCreatedAt().toString()))
			.andExpect(jsonPath("$.hasNext").value(false));
	}

	@DisplayName("[정렬 조건에 따라 경매글 목록을 반환한다.]")
	@Test
	void getRecommendAuctionsWithOutFilter() throws Exception {
		//given
		Auction auction1 = AuctionFixture.auction(productCategory);
		Auction auction2 = AuctionFixture.auction(productCategory);
		Auction auction3 = AuctionFixture.auction(productCategory);
		auctionRepository.saveAll(List.of(auction1, auction2, auction3));

		//when
		mockMvc.perform(get("/api/auctions/recommend")
				.param("sort", "최근생성")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size").value(3))
			.andExpect(jsonPath("$.content[0].auctionId").value(auction3.getId()))
			.andExpect(jsonPath("$.content[1].auctionId").value(auction2.getId()))
			.andExpect(jsonPath("$.content[2].auctionId").value(auction1.getId()))
			.andExpect(jsonPath("$.hasNext").value(false));
	}

	@DisplayName("[정렬 조건이 없을 시 예외를 반환한다.]")
	@Test
	void getRecommendAuctions_fails() throws Exception {
		mockMvc.perform(get("/api/auctions/recommend")
				.contentType(APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.message").value(AuctionErrorCode.EMPTY_SORT_INPUT.getMessage()))
			.andExpect(jsonPath("$.code").value(AuctionErrorCode.EMPTY_SORT_INPUT.getCode()));
	}

	@DisplayName("[정렬 조건이 잘못되면 예외를 반환한다.]")
	@Test
	void getRecommendAuctions_fails2() throws Exception {
		mockMvc.perform(get("/api/auctions/recommend")
				.contentType(APPLICATION_JSON)
				.param("sort", "NAN"))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("$.message").value(AuctionErrorCode.INVALID_SORT_INPUT.getMessage()))
			.andExpect(jsonPath("$.code").value(AuctionErrorCode.INVALID_SORT_INPUT.getCode()));
	}
}
