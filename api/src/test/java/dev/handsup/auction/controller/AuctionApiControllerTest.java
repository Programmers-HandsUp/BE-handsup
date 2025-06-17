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

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.auction_field.TradingLocation;
import dev.handsup.auction.domain.product.Product;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;

@DisplayName("[Auction 통합 테스트]")
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
		userRepository.save(user);
	}

	@DisplayName("[경매를 등록할 수 있다.]")
	@Test
	void registerAuction() throws Exception {
		RegisterAuctionRequest request = RegisterAuctionRequest.of("거의 새상품 버즈 팔아요", DIGITAL_DEVICE, 10000,
			LocalDate.parse("2022-10-18"), ProductStatus.NEW.getLabel(), PurchaseTime.UNDER_ONE_MONTH.getLabel(),
			"거의 새상품이에요", TradeMethod.DELIVER.getLabel(), List.of("test.jpg"));

		mockMvc.perform(post("/api/auctions").header(AUTHORIZATION, "Bearer " + accessToken)
				.content(toJson(request))
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.sellerInfo.userId").value(user.getId()))
			.andExpect(jsonPath("$.title").value(request.title()))
			.andExpect(jsonPath("$.description").value(request.description()))
			.andExpect(jsonPath("$.productStatus").value(request.productStatus()))
			.andExpect(jsonPath("$.tradeMethod").value(request.tradeMethod()))
			.andExpect(jsonPath("$.endDate").value(request.endDate().atStartOfDay().toString()))
			.andExpect(jsonPath("$.initPrice").value(request.initPrice()))
			.andExpect(jsonPath("$.purchaseTime").value(request.purchaseTime()))
			.andExpect(jsonPath("$.productCategory").value(request.productCategory()))
			.andExpect(jsonPath("$.imageUrls[0]").value(request.imageUrls().get(0)))
			.andExpect(jsonPath("$.tradeSi").isEmpty())
			.andExpect(jsonPath("$.tradeGu").isEmpty())
			.andExpect(jsonPath("$.tradeDong").isEmpty());
	}

	@DisplayName("[경매를 등록 시 상품 카테고리가 DB에 없으면 예외가 발생한다.]")
	@Test
	void registerAuctionFails() throws Exception {
		final String NOT_EXIST_CATEGORY = "아";
		RegisterAuctionRequest request = RegisterAuctionRequest.of("거의 새상품 버즈 팔아요", NOT_EXIST_CATEGORY, 10000,
			LocalDate.parse("2022-10-18"), ProductStatus.NEW.getLabel(), PurchaseTime.UNDER_ONE_MONTH.getLabel(),
			"거의 새상품이에요", TradeMethod.DELIVER.getLabel(), List.of("test.jpg"), "서울시", "성북구", "동선동");

		mockMvc.perform(post("/api/auctions").header(AUTHORIZATION, "Bearer " + accessToken)
				.content(toJson(request))
				.contentType(APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY.getMessage()))
			.andExpect(jsonPath("$.code").value(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY.getCode()));
	}

	@DisplayName("[경매 상세정보를 조회할 수 있다.]")
	@Test
	void getAuctionDetail() throws Exception {
		//given
		Auction auction = auctionRepository.save(AuctionFixture.auction(productCategory));
		Product product = auction.getProduct();
		TradingLocation tradingLocation = auction.getTradingLocation();

		//when, then
		mockMvc.perform(get("/api/auctions/{auctionId}", auction.getId()).contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.auctionId").value(auction.getId()))
			.andExpect(jsonPath("$.sellerInfo.userId").value(user.getId()))
			.andExpect(jsonPath("$.sellerInfo.nickname").value(user.getNickname()))
			.andExpect(jsonPath("$.sellerInfo.profileImageUrl").value(user.getProfileImageUrl()))
			.andExpect(jsonPath("$.sellerInfo.userId").value(user.getId()))
			.andExpect(jsonPath("$.sellerInfo.score").value(user.getScore()))
			.andExpect(jsonPath("$.sellerInfo.dong").value(user.getAddress().getDong()))
			.andExpect(jsonPath("$.title").value(auction.getTitle()))
			.andExpect(
				jsonPath("$.productCategory").value(product.getProductCategory().getValue()))
			.andExpect(jsonPath("$.initPrice").value(auction.getInitPrice()))
			.andExpect(jsonPath("$.currentBiddingPrice").value(auction.getCurrentBiddingPrice()))
			.andExpect(jsonPath("$.endDate").value(auction.getEndDate().atStartOfDay().toString()))
			.andExpect(jsonPath("$.productStatus").value(product.getStatus().getLabel()))
			.andExpect(jsonPath("$.purchaseTime").value(product.getPurchaseTime().getLabel()))
			.andExpect(jsonPath("$.description").value(product.getDescription()))
			.andExpect(jsonPath("$.tradeMethod").value(auction.getTradeMethod().getLabel()))
			.andExpect(jsonPath("$.imageUrls[0]").value(product.getImages().get(0).getImageUrl()))
			.andExpect(jsonPath("$.tradeSi").value(tradingLocation.getSi()))
			.andExpect(jsonPath("$.tradeGu").value(tradingLocation.getGu()))
			.andExpect(jsonPath("$.tradeDong").value(tradingLocation.getDong()))
			.andExpect(jsonPath("$.bookmarkCount").value(auction.getBookmarkCount()));
	}
}
