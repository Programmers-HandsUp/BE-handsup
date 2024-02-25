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
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.repository.BiddingRepository;
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
	@Autowired
	private BiddingRepository biddingRepository;

	private Auction auction;
	private ProductCategory productCategory;

	@BeforeEach
	void setUp() {
		String DIGITAL_DEVICE = "디지털 기기";
		productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(productCategory);
		auction = auctionRepository.save(AuctionFixture.auction(productCategory));
		// 전체 테스트시 user 가 db 에서 삭제되는 오류
		userRepository.save(user);
	}

	@Test
	@DisplayName("[[입찰 등록 API] 사용자가 경매에 입찰을 등록한다]")
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

	@DisplayName("[[입찰 목록 전체 조회 API] 한 경매의 모든 입찰 목록을 입찰가 기준 내림차순으로 조회한다]")
	@Test
	void getBidsOfAuctionTest() throws Exception {
		// given
		Auction auction2 = AuctionFixture.auction(productCategory);
		auctionRepository.save(auction2);
		biddingRepository.save(Bidding.of(40000, auction, user));
		biddingRepository.save(Bidding.of(20000, auction, user));
		biddingRepository.save(Bidding.of(30000, auction, user));
		biddingRepository.save(Bidding.of(50000, auction, user));
		biddingRepository.save(Bidding.of(60000, auction2, user));
		biddingRepository.save(Bidding.of(10000, auction2, user));
		Long auctionId = auction.getId();
		int page = 0;
		int size = 10;

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get(
					"/api/auctions/{auctionId}/bids?page={page}&size={size}", auctionId, page, size)
				.contentType(APPLICATION_JSON)
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.content.size()").value(4),
			jsonPath("$.content[0].biddingPrice").value(50000),
			jsonPath("$.content[1].biddingPrice").value(40000),
			jsonPath("$.content[2].biddingPrice").value(30000),
			jsonPath("$.content[3].biddingPrice").value(20000)
		);
	}

	@DisplayName("[[입찰 목록 상위 3개 조회 API] 한 경매의 입찰 목록 중에서 입찰가 기준 내림차순으로 3개를 조회한다]")
	@Test
	void getTop3BidsForAuctionTest() throws Exception {
		Auction auction2 = AuctionFixture.auction(productCategory);
		auctionRepository.save(auction2);
		biddingRepository.save(Bidding.of(10000, auction, user));
		biddingRepository.save(Bidding.of(30000, auction, user));
		biddingRepository.save(Bidding.of(20000, auction, user));
		biddingRepository.save(Bidding.of(60000, auction, user));
		biddingRepository.save(Bidding.of(50000, auction, user));
		biddingRepository.save(Bidding.of(70000, auction2, user));
		biddingRepository.save(Bidding.of(50000, auction2, user));
		Long auctionId = auction.getId();

		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/auctions/{auctionId}/bids/top-3-bids", auctionId)
				.contentType(APPLICATION_JSON)
		);

		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.content.size()").value(3),
			jsonPath("$.content[0].biddingPrice").value(60000),
			jsonPath("$.content[1].biddingPrice").value(50000),
			jsonPath("$.content[2].biddingPrice").value(30000)
		);
	}

}
