package dev.handsup.review.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.domain.product.product_category.ProductCategoryValue;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.bidding.service.BiddingService;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ReviewFixture;
import dev.handsup.fixture.ReviewLabelFixture;
import dev.handsup.notification.repository.FCMTokenRepository;
import dev.handsup.review.domain.Review;
import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.domain.ReviewLabelValue;
import dev.handsup.review.dto.request.RegisterReviewRequest;
import dev.handsup.review.dto.response.ReviewDetailResponse;
import dev.handsup.review.repository.ReviewLabelRepository;
import dev.handsup.review.repository.ReviewRepository;
import dev.handsup.user.domain.User;
import dev.handsup.user.exception.UserErrorCode;
import dev.handsup.user.repository.UserRepository;

@DisplayName("[Review 통합 테스트]")
class ReviewApiControllerTest extends ApiTestSupport {

	private final Review review = ReviewFixture.review(1L);
	private final ReviewLabel reviewLabelManner = ReviewLabelFixture.reviewLabel(
		1L, ReviewLabelValue.MANNER.getLabel()
	);
	private final ReviewLabel reviewLabelCheap = ReviewLabelFixture.reviewLabel(
		2L, ReviewLabelValue.CHEAP_PRICE.getLabel()
	);
	private final List<Long> reviewLabelIds = List.of(1L, 2L);
	private Auction auction;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ReviewLabelRepository reviewLabelRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	@Autowired
	private BiddingRepository biddingRepository;
	@Autowired
	private BiddingService biddingService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FCMTokenRepository fcmTokenRepository;

	@BeforeEach
	void setUp() {
		productCategoryRepository.save(review.getAuction().getProduct().getProductCategory());
		auction = auctionRepository.save(review.getAuction());
		reviewLabelRepository.saveAll(List.of(reviewLabelManner, reviewLabelCheap));
	}

	@Test
	@Transactional
	@DisplayName("[리뷰 등록 API] 작성자가 경매에 대한 리뷰를 등록한다")
	void registerReviewTest() throws Exception {
		// given
		fcmTokenRepository.saveFcmToken(auction.getSeller().getEmail(), "fcmToken123");
		ReflectionTestUtils.setField(auction, "status", AuctionStatus.TRADING);
		Long auctionId = auction.getId();
		int beforeSellerScore = auction.getSeller().getScore();
		ReflectionTestUtils.setField(review, "createdAt", LocalDateTime.now());

		Bidding bidding = new Bidding(
			this.auction.getInitPrice() + 1000, this.auction, user, TradingStatus.PROGRESSING);
		biddingRepository.save(bidding);
		biddingService.completeTrading(bidding.getId(), user);

		RegisterReviewRequest request = RegisterReviewRequest.of(
			review.getEvaluationScore(),
			review.getContent(),
			reviewLabelIds
		);

		ReviewDetailResponse expectedResponse = ReviewDetailResponse.of(
			review.getId(),
			review.getEvaluationScore(),
			review.getContent(),
			auctionId,
			user.getId(),
			user.getNickname(),
			this.auction.getTitle(),
			bidding.getBiddingPrice(),
			this.auction.getTradeMethod().toString(),
			this.auction.getTradingLocation(),
			bidding.getUpdatedAt().toString(),
			review.getCreatedAt().toString()
		);

		// when & then
		ResultActions resultActions = mockMvc.perform(post("/api/auctions/{auctionId}/reviews", auctionId)
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON)
				.content(toJson(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.reviewId").value(expectedResponse.reviewId()))
			.andExpect(jsonPath("$.evaluationScore").value(expectedResponse.evaluationScore()))
			.andExpect(jsonPath("$.content").value(expectedResponse.content()))
			.andExpect(jsonPath("$.auctionId").value(expectedResponse.auctionId()))
			.andExpect(jsonPath("$.writerId").value(expectedResponse.writerId()))
			.andExpect(jsonPath("$.writerNickname").value(expectedResponse.writerNickname()))
			.andExpect(jsonPath("$.auctionTitle").value(expectedResponse.auctionTitle()))
			.andExpect(jsonPath("$.winningPrice").value(expectedResponse.winningPrice()))
			.andExpect(jsonPath("$.tradeMethod").value(expectedResponse.tradeMethod()))
			.andExpect(jsonPath("$.tradingLocation.si").value(expectedResponse.tradingLocation().getSi()))
			.andExpect(jsonPath("$.tradingLocation.gu").value(expectedResponse.tradingLocation().getGu()))
			.andExpect(jsonPath("$.tradingLocation.dong").value(expectedResponse.tradingLocation().getDong()));

		User evaluatedSeller = userRepository.findById(this.auction.getSeller().getId())
			.orElseThrow(() -> new NotFoundException(UserErrorCode.NOT_FOUND_USER));

		assertThat(evaluatedSeller.getScore())
			.isEqualTo(beforeSellerScore + request.evaluationScore());

		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		ReviewDetailResponse actualResponse = objectMapper.readValue(responseBody, ReviewDetailResponse.class);
		LocalDateTime expectedTime = LocalDateTime.parse(expectedResponse.tradingCreatedAt());
		LocalDateTime actualTime = LocalDateTime.parse(actualResponse.tradingCreatedAt());
		long secondsBetween = Duration.between(expectedTime, actualTime).getSeconds();

		assertThat(Math.abs(secondsBetween)).isLessThan(1);
	}

	@Test
	@DisplayName("[경매 리뷰 조회 API] 해당 경매의 모든 리뷰를 최신 등록순으로 조회해온다.")
	void getReviewsOfAuctionTest() throws Exception {
		// given
		Auction auction1 = auction;
		ProductCategory productCategoryBooks = ProductCategory.from(ProductCategoryValue.BOOKS.toString());
		productCategoryRepository.save(productCategoryBooks);
		Auction auction2 = AuctionFixture.auction(productCategoryBooks);
		auctionRepository.save(auction2);

		Review review1 = ReviewFixture.review("content1", auction1);
		Review review2 = ReviewFixture.review("content2", auction1);
		Review review3 = ReviewFixture.review("content3", auction1);
		Review review4 = ReviewFixture.review("content4", auction2);
		reviewRepository.saveAll(
			List.of(review3, review1, review2, review4)    // 처음부터 3, 1, 2, 4 순서로 create
		);

		// when & then
		mockMvc.perform(get("/api/auctions/{auctionId}/reviews", auction1.getId())
				.header(AUTHORIZATION, "Bearer " + accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content.size()").value(3))
			.andExpect(jsonPath("$.content[0].content").value("content2"))
			.andExpect(jsonPath("$.content[0].auctionId").value(auction1.getId()))
			.andExpect(jsonPath("$.content[1].content").value("content1"))
			.andExpect(jsonPath("$.content[1].auctionId").value(auction1.getId()))
			.andExpect(jsonPath("$.content[2].content").value("content3"))
			.andExpect(jsonPath("$.content[2].auctionId").value(auction1.getId()));
	}
}
