package dev.handsup.auction.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.RegisterAuctionApiRequest;
import dev.handsup.auction.repository.AuctionRepository;
import dev.handsup.auction.repository.ProductCategoryRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.ProductFixture;

class AuctionControllerTest extends ApiTestSupport {

	private final String DIGITAL_DEVICE = "디지털 기기";
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@BeforeEach
	void setUp() {
		ProductCategory productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(productCategory);
	}

	@DisplayName("경매를 등록할 수 있다.")
	@Test
	void registerAuction() throws Exception {
		RegisterAuctionApiRequest request = RegisterAuctionApiRequest.builder()
			.title("아이패드 팔아요")
			.description("아이패드 팔아요. 오래 되어서 싸게 팔아요")
			.productStatus(ProductStatus.DIRTY.getLabel())
			.tradeMethod(TradeMethod.DIRECT.getLabel())
			.endDate(LocalDate.parse("2023-02-19"))
			.initPrice(300000)
			.purchaseTime(PurchaseTime.ABOVE_ONE_YEAR.getLabel())
			.productCategory(DIGITAL_DEVICE)
			.build();

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
}