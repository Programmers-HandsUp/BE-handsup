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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.auction.service.AuctionService;
import dev.handsup.auth.service.JwtProvider;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.exception.BiddingErrorCode;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.BiddingFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserRepository;

@DisplayName("[BiddingApiController 테스트]")
class BiddingApiControllerTest extends ApiTestSupport {

	private final User seller = user;
	private final User bidder = UserFixture.user(2L, "bidder@naver.com");
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	@Autowired
	private BiddingRepository biddingRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuctionService auctionService;
	@Autowired
	private JwtProvider jwtProvider;
	private String sellerAccessToken;
	private String bidderAccessToken;
	private Auction auction1;
	private Auction auction2;

	@BeforeEach
	void setUp() {
		userRepository.save(bidder);
		sellerAccessToken = accessToken;
		bidderAccessToken = jwtProvider.createAccessToken(bidder.getId());

		auction1 = AuctionFixture.auction(seller);
		productCategoryRepository.save(auction1.getProduct().getProductCategory());
		auctionRepository.save(auction1);

		auction2 = AuctionFixture.auction(seller);
		ReflectionTestUtils.setField(auction2, "id", 2L);
		productCategoryRepository.save(auction2.getProduct().getProductCategory());
		auctionRepository.save(auction2);
	}

	@Test
	@DisplayName("[[입찰 등록 API] 사용자가 경매에 입찰을 등록한다]")
	void registerBiddingTest() throws Exception {
		// given
		RegisterBiddingRequest request = RegisterBiddingRequest.from(10000);
		int beforeBiddingCount = auction1.getBiddingCount();

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/auctions/{auctionId}/bids", auction1.getId())
				.header(AUTHORIZATION, "Bearer " + bidderAccessToken)
				.contentType(APPLICATION_JSON)
				.content(toJson(request))
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),
			jsonPath("$.biddingPrice").value(10000),
			jsonPath("$.auctionId").value(auction1.getId()),
			jsonPath("$.bidderId").value(bidder.getId()),
			jsonPath("$.bidderNickname").value(bidder.getNickname()),
			jsonPath("$.tradingStatus").value(TradingStatus.WAITING.getLabel()),
			jsonPath("$.imgUrl").value(bidder.getProfileImageUrl())
		);

		auction1 = auctionService.getAuctionById(this.auction1.getId());
		assertThat(this.auction1.getBiddingCount()).isEqualTo(beforeBiddingCount + 1);
	}

	@DisplayName("[[입찰 목록 전체 조회 API] 한 경매의 모든 입찰 목록을 입찰가 기준 내림차순으로 조회한다]")
	@Test
	void getBidsOfAuctionTest() throws Exception {
		// given
		List<Bidding> biddingList = List.of(
			Bidding.of(40000, auction1, bidder),
			Bidding.of(20000, auction1, bidder),
			Bidding.of(30000, auction1, bidder),
			Bidding.of(50000, auction2, bidder)
		);

		biddingRepository.saveAll(biddingList);

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get(
					"/api/auctions/{auctionId}/bids", auction1.getId())
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
		List<Bidding> biddingList = List.of(
			Bidding.of(10000, auction1, bidder),
			Bidding.of(30000, auction1, bidder),
			Bidding.of(20000, auction1, bidder),
			Bidding.of(60000, auction1, bidder),
			Bidding.of(50000, auction1, bidder),
			Bidding.of(70000, auction2, bidder)
		);
		biddingRepository.saveAll(biddingList);

		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/auctions/{auctionId}/bids/top3", auction1.getId())
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

	@Transactional
	@DisplayName("[판매자는 진행 중인 거래를 완료 상태로 변경할 수 있다.]")
	@Test
	void completeTrading() throws Exception {
		//given
		ReflectionTestUtils.setField(auction1, "status", AuctionStatus.TRADING); //변경 감지
		Bidding bidding = BiddingFixture.bidding(bidder, auction1, TradingStatus.PROGRESSING);
		biddingRepository.save(bidding);
		//when, then
		mockMvc.perform(patch("/api/auctions/bids/{biddingId}/complete", bidding.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, "Bearer " + sellerAccessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tradingStatus").value(TradingStatus.COMPLETED.getLabel()))
			.andExpect(jsonPath("$.auctionId").value(auction1.getId()))
			.andExpect(jsonPath("$.bidderId").value(bidder.getId()));
	}

	@DisplayName("[판매자는 거래 상태가 진행중이 아니라면 거래를 완료할 수 없다.]")
	@Test
	void completeTrading_fails() throws Exception {
		//given
		Bidding bidding = BiddingFixture.bidding(bidder, auction1, TradingStatus.WAITING);
		biddingRepository.save(bidding);
		//when, then
		mockMvc.perform(patch("/api/auctions/bids/{biddingId}/complete", bidding.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, "Bearer " + sellerAccessToken))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code")
				.value(BiddingErrorCode.CAN_NOT_COMPLETE_TRADING.getCode()));
	}

	@Transactional
	@DisplayName("[판매자는 진행 중인 거래를 취소 상태로 변경할 수 있다.]")
	@Test
	void cancelTrading() throws Exception {
		//given
		Bidding waitingBidding1 = BiddingFixture.bidding(bidder, auction1, TradingStatus.WAITING, 200000);
		Bidding waitingBidding2 = BiddingFixture.bidding(bidder, auction1, TradingStatus.WAITING, 300000);
		Bidding anotherAuctionBidding = BiddingFixture.bidding(bidder, auction2, TradingStatus.WAITING, 400000);
		Bidding progressingBidding = BiddingFixture.bidding(bidder, auction1, TradingStatus.PROGRESSING, 500000);
		biddingRepository.saveAll(List.of(waitingBidding1, waitingBidding2, anotherAuctionBidding, progressingBidding));

		//when, then
		mockMvc.perform(patch("/api/auctions/bids/{biddingId}/cancel", progressingBidding.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, "Bearer " + sellerAccessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tradingStatus").value(TradingStatus.CANCELED.getLabel()))
			.andExpect(jsonPath("$.auctionId").value(auction1.getId()))
			.andExpect(jsonPath("$.bidderId").value(bidder.getId()));

		assertThat(waitingBidding2.getTradingStatus()).isEqualTo(TradingStatus.PREPARING); // 변경 감지 위해 @Transactional 필요
	}

	@DisplayName("[판매자는 거래가 진행중이 아니면 취소할 수 없다.]")
	@Test
	void cancelTrading_fails() throws Exception {
		//given
		Bidding bidding = BiddingFixture.bidding(bidder, auction1, TradingStatus.COMPLETED);
		biddingRepository.save(bidding);
		//when, then
		mockMvc.perform(patch("/api/auctions/bids/{biddingId}/cancel", bidding.getId())
				.contentType(APPLICATION_JSON)
				.header(AUTHORIZATION, "Bearer " + sellerAccessToken))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code")
				.value(BiddingErrorCode.CAN_NOT_CANCEL_TRADING.getCode()));
	}
}
