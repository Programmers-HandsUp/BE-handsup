package dev.handsup.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.common.service.EntityManagementService;
import dev.handsup.fixture.AuctionFixture;
import dev.handsup.fixture.ReviewFixture;
import dev.handsup.fixture.ReviewLabelFixture;
import dev.handsup.fixture.UserFixture;
import dev.handsup.fixture.UserReviewLabelFixture;
import dev.handsup.review.domain.Review;
import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.domain.ReviewLabelValue;
import dev.handsup.review.domain.UserReviewLabel;
import dev.handsup.review.dto.request.RegisterReviewRequest;
import dev.handsup.review.dto.response.ReviewResponse;
import dev.handsup.review.repository.ReviewInterReviewLabelRepository;
import dev.handsup.review.repository.ReviewLabelRepository;
import dev.handsup.review.repository.ReviewRepository;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserReviewLabelRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("[ReviewService 테스트]")
class ReviewServiceTest {

	private final Auction auction = AuctionFixture.auction();
	private final User writer = UserFixture.user();
	private final Review review = ReviewFixture.review();
	private final ReviewLabel reviewLabelManner = ReviewLabelFixture.reviewLabel(
		1L, ReviewLabelValue.MANNER.getDescription()
	);
	private final ReviewLabel reviewLabelCheap = ReviewLabelFixture.reviewLabel(
		2L, ReviewLabelValue.CHEAP.getDescription()
	);
	private final List<Long> reviewLabelIds = List.of(1L, 2L);
	private final UserReviewLabel userReviewLabelManner = UserReviewLabelFixture.userReviewLabel(
		1L, reviewLabelManner
	);
	private final UserReviewLabel userReviewLabelCheap = UserReviewLabelFixture.userReviewLabel(
		2L, reviewLabelCheap
	);
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private ReviewLabelRepository reviewLabelRepository;
	@Mock
	private ReviewInterReviewLabelRepository reviewInterReviewLabelRepository;
	@Mock
	private UserReviewLabelRepository userReviewLabelRepository;
	@Mock
	private AuctionRepository auctionRepository;
	@Mock
	private EntityManagementService entityManagementService;
	@InjectMocks
	private ReviewService reviewService;

	@Test
	@MockitoSettings(strictness = Strictness.LENIENT)
	@DisplayName("[리뷰 등록이 성공적으로 이루어진다]")
	void registerReview_Success() {
		// given
		int expectedEvaluationScore = review.getEvaluationScore();
		String expectedContent = review.getContent();
		Long expectedAuctionId = auction.getId();
		Long expectedWriterId = writer.getId();

		RegisterReviewRequest request = RegisterReviewRequest.of(
			review.getEvaluationScore(),
			review.getContent(),
			reviewLabelIds
		);
		Long auctionId = auction.getId();
		given(entityManagementService.getEntity(auctionRepository, auctionId)).willReturn(auction);
		given(reviewRepository.save(any(Review.class))).willReturn(review);

		given(entityManagementService.getEntity(reviewLabelRepository, reviewLabelManner.getId())).willReturn(
			reviewLabelManner);
		given(entityManagementService.getEntity(reviewLabelRepository, reviewLabelCheap.getId())).willReturn(
			reviewLabelCheap);
		given(reviewInterReviewLabelRepository.save(any())).willReturn(null);
		given(userReviewLabelRepository.save(any(UserReviewLabel.class)))
			.willReturn(userReviewLabelManner, userReviewLabelCheap);
		given(entityManagementService.getEntity(userReviewLabelRepository, userReviewLabelManner.getId())).willReturn(
			userReviewLabelManner
		);
		given(entityManagementService.getEntity(userReviewLabelRepository, userReviewLabelCheap.getId())).willReturn(
			userReviewLabelCheap
		);

		// when
		ReviewResponse response = reviewService.registerReview(request, auctionId, writer);

		// then
		assertThat(response).isNotNull();
		assertThat(response.evaluationScore()).isEqualTo(expectedEvaluationScore);
		assertThat(response.content()).isEqualTo(expectedContent);
		assertThat(response.auctionId()).isEqualTo(expectedAuctionId);
		assertThat(response.writerId()).isEqualTo(expectedWriterId);
	}
}
