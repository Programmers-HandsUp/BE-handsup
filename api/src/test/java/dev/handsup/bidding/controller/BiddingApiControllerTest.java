package dev.handsup.bidding.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;

@DisplayName("[BiddingApiController 테스트]")
class BiddingApiControllerTest extends ApiTestSupport {

	private final User user = UserFixture.user();
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	@Autowired
	private UserRepository userRepository;
	private Auction auction;

	@BeforeEach
	void setUp() {
		String DIGITAL_DEVICE = "디지털 기기";
		ProductCategory productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(productCategory);
		auction = auctionRepository.save(AuctionFixture.auction(productCategory));
		// 전체 테스트시 user 가 db 에서 삭제되는 오류
		userRepository.save(user);
	}

	@Test
	@DisplayName("[입찰 등록 API를 호출하면 입찰이 등록되고 입찰을 응답한다]")
	void registerBiddingTest() throws Exception {
		// given
		RegisterBiddingRequest request = RegisterBiddingRequest.from(10000);

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/auctions/{auctionId}/bids", auction.getId())
				.header(AUTHORIZATION, accessToken)
				.contentType(APPLICATION_JSON)
				.content(toJson(request))
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.biddingPrice").value(10000),
			jsonPath("$.auctionId").value(auction.getId()),
			jsonPath("$.bidderNickname").value(user.getNickname())
		);
	}

}
