package dev.handsup.bidding.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.exception.BiddingErrorCode;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BiddingFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;

@DisplayName("[BiddingApiController 테스트]")
class BiddingApiControllerTest extends ApiTestSupport {

	private final User bidder = UserFixture.user("bidder@gmail.com");
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
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
		userRepository.saveAll(List.of(user, bidder));
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
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON)
				.content(toJson(request))
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.biddingPrice").value(10000),
			jsonPath("$.auctionId").value(auction.getId()),
			jsonPath("$.bidderId").value(user.getId()),
			jsonPath("$.bidderNickname").value(user.getNickname()),
			jsonPath("$.tradingStatus").value(TradingStatus.PREPARING.getLabel()),
			jsonPath("$.imgUrl").value(auction.getProduct().getImages().get(0).getImageUrl())
		);
	}

	@DisplayName("[[입찰 목록 전체 조회 API] 한 경매의 모든 입찰 목록을 입찰가 기준 내림차순으로 조회한다]")
	@Test
	void getBidsOfAuctionTest() throws Exception {
		// given
		Auction auction2 = AuctionFixture.auction(productCategory);
		auctionRepository.save(auction2);
		List<Bidding> biddingList = List.of(
			Bidding.of(40000, auction, user),
			Bidding.of(20000, auction, user),
			Bidding.of(30000, auction, user),
			Bidding.of(50000, auction2, user)
		);

		biddingRepository.saveAll(biddingList);

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get(
					"/api/auctions/{auctionId}/bids", auction.getId())
				.contentType(APPLICATION_JSON)
				.param("page", "0")
				.param("size", "10")
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.content.size()").value(3),
			jsonPath("$.content[0].biddingPrice").value(40000),
			jsonPath("$.content[1].biddingPrice").value(30000),
			jsonPath("$.content[2].biddingPrice").value(20000)
		).andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("[[입찰 목록 상위 3개 조회 API] 한 경매의 입찰 목록 중에서 입찰가 기준 내림차순으로 3개를 조회한다]")
	@Test
	void getTop3BidsForAuctionTest() throws Exception {
		Auction auction2 = AuctionFixture.auction(productCategory);
		auctionRepository.save(auction2);
		List<Bidding> biddingList = List.of(
			Bidding.of(10000, auction, user),
			Bidding.of(30000, auction, user),
			Bidding.of(20000, auction, user),
			Bidding.of(60000, auction, user),
			Bidding.of(50000, auction, user),
			Bidding.of(70000, auction2, user)
		);
		biddingRepository.saveAll(biddingList);

		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/auctions/{auctionId}/bids/top3", auction.getId())
				.contentType(APPLICATION_JSON)
		);

		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.content.size()").value(3),
			jsonPath("$.content[0].biddingPrice").value(60000),
			jsonPath("$.content[1].biddingPrice").value(50000),
			jsonPath("$.content[2].biddingPrice").value(30000)
		).andDo(MockMvcResultHandlers.print());
	}

	@DisplayName("[판매자는 진행 중인 거래를 완료 상태로 변경할 수 있다.]")
	@Test
	void completeTrading() throws Exception {
		//given
		Bidding bidding = BiddingFixture.bidding(bidder, auction, TradingStatus.PROGRESSING);
		biddingRepository.save(bidding);
		//when, then
		mockMvc.perform(patch("/api/auctions/bids/{biddingId}/complete", bidding.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tradingStatus").value(TradingStatus.COMPLETED.getLabel()))
			.andExpect(jsonPath("$.auctionId").value(auction.getId()))
			.andExpect(jsonPath("$.bidderId").value(bidder.getId()));
	}

	@DisplayName("[거래 상태가 진행중이 아니라면 거래를 완료할 수 없다.]")
	@Test
	void completeTrading_fails() throws Exception {
		//given
		Bidding bidding = BiddingFixture.bidding(bidder, auction, TradingStatus.WAITING);
		biddingRepository.save(bidding);
		//when, then
		mockMvc.perform(patch("/api/auctions/bids/{biddingId}/complete", bidding.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, "Bearer " + accessToken))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code")
				.value(BiddingErrorCode.CAN_NOT_COMPLETE_TRADING.getCode()));
	}

	@Transactional
	@DisplayName("[판매자는 진행 중인 거래를 취소 상태로 변경할 수 있다.]")
	@Test
	void cancelTrading() throws Exception {
		//given
		Bidding bidding1 = BiddingFixture.bidding(bidder, auction, TradingStatus.PROGRESSING);
		Bidding bidding2 = BiddingFixture.bidding(bidder, auction, TradingStatus.WAITING);
		biddingRepository.saveAll(List.of(bidding1, bidding2));
		//when, then
		mockMvc.perform(patch("/api/auctions/bids/{biddingId}/cancel", bidding1.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tradingStatus").value(TradingStatus.CANCELED.getLabel()))
			.andExpect(jsonPath("$.auctionId").value(auction.getId()))
			.andExpect(jsonPath("$.bidderId").value(bidder.getId()));

		assertThat(bidding2.getStatus()).isEqualTo(TradingStatus.PREPARING); // 변경 감지 위해 @Transactional 필요
	}

	@DisplayName("[거래가 진행중이 아니면 취소할 수 없다.]")
	@Test
	void cancelTrading_fails() throws Exception {
		//given
		Bidding bidding = BiddingFixture.bidding(bidder, auction, TradingStatus.COMPLETED);
		biddingRepository.save(bidding);
		//when, then
		mockMvc.perform(patch("/api/auctions/bids/{biddingId}/cancel", bidding.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, "Bearer " + accessToken))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code")
				.value(BiddingErrorCode.CAN_NOT_CANCEL_TRADING.getCode()));
	}
}
