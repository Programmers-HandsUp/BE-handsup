package dev.handsup.bidding.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.AuctionRepository;
import dev.handsup.auction.repository.ProductCategoryRepository;
import dev.handsup.bidding.dto.RegisterBiddingApiRequest;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

@DisplayName("[BiddingApiController 테스트]")
class BiddingApiControllerTest extends ApiTestSupport {

	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	private final String DIGITAL_DEVICE = "디지털 기기";

	private User user = UserFixture.user();
	private Auction auction = AuctionFixture.auction();
	private RegisterBiddingApiRequest request = RegisterBiddingApiRequest.from(10000);

	@BeforeEach
	void setUp() {
		ProductCategory productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(productCategory);
	}

	@Test
	@DisplayName("[입찰 등록 API를 호출하면 입찰이 등록되고 입찰을 응답한다]")
	void registerBiddingTest() throws Exception {
		//given
		auctionRepository.save(auction);

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/auctions/" + auction.getId() + "/bids")
				.header(AUTHORIZATION, accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(request))
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),

			jsonPath("$.biddingPrice").value(10000),
			jsonPath("$.bidderId").value(user.getId())
		);
	}

}
