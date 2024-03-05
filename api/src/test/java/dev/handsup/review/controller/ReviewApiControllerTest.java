package dev.handsup.review.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.support.ApiTestSupport;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ProductFixture;
import dev.handsup.fixture.ReviewFixture;
import dev.handsup.fixture.ReviewLabelFixture;
import dev.handsup.review.domain.Review;
import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.domain.ReviewLabelValue;
import dev.handsup.review.dto.request.RegisterReviewRequest;
import dev.handsup.review.dto.response.ReviewResponse;
import dev.handsup.review.repository.ReviewLabelRepository;
import dev.handsup.review.repository.ReviewRepository;
import jakarta.persistence.EntityManager;

@DisplayName("[Review 통합 테스트]")
class ReviewApiControllerTest extends ApiTestSupport {

	private final Review review = ReviewFixture.review();
	private final ReviewLabel reviewLabelManner = ReviewLabelFixture.reviewLabel(
		1L, ReviewLabelValue.MANNER.getDescription()
	);
	private final ReviewLabel reviewLabelCheap = ReviewLabelFixture.reviewLabel(
		2L, ReviewLabelValue.CHEAP.getDescription()
	);
	private final List<Long> reviewLabelIds = List.of(1L, 2L);
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ReviewLabelRepository reviewLabelRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	private ProductCategory productCategory;
	private Auction auction;

	@BeforeEach
	void setUp() {
		String DIGITAL_DEVICE = "디지털 기기";
		productCategory = ProductFixture.productCategory(DIGITAL_DEVICE);
		productCategoryRepository.save(productCategory);
		auction = auctionRepository.save(AuctionFixture.auction(productCategory));
		reviewLabelRepository.saveAll(List.of(reviewLabelManner, reviewLabelCheap));
	}

	@Test
	@DisplayName("[리뷰 등록 API] 작성자가 경매에 대한 리뷰를 등록한다")
	void registerReviewTest() throws Exception {
		// given
		Long auctionId = 1L;
		RegisterReviewRequest request = RegisterReviewRequest.of(
			review.getEvaluationScore(),
			review.getContent(),
			reviewLabelIds
		);
		ReviewResponse expectedResponse = ReviewResponse.of(
			review.getEvaluationScore(),
			review.getContent(),
			auctionId,
			user.getId()
		);

		// when & then
		mockMvc.perform(post("/api/auctions/{auctionId}/reviews", auctionId)
				.header(AUTHORIZATION, "Bearer " + accessToken)
				.contentType(APPLICATION_JSON)
				.content(toJson(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.evaluationScore").value(expectedResponse.evaluationScore()))
			.andExpect(jsonPath("$.content").value(expectedResponse.content()))
			.andExpect(jsonPath("$.auctionId").value(expectedResponse.auctionId()))
			.andExpect(jsonPath("$.writerId").value(expectedResponse.writerId()));
	}

	@Test
	@DisplayName("[경매 리뷰 조회 API] 해당 경매의 모든 리뷰를 최신 등록순으로 조회해온다.")
	void getReviewsOfAuctionTest() throws Exception {
		// given
		Auction auction1 = auction;
		Auction auction2 = AuctionFixture.auction(productCategory);
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
