package dev.handsup.review.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.service.EntityManagementService;
import dev.handsup.review.domain.Review;
import dev.handsup.review.domain.ReviewInterReviewLabel;
import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.domain.UserReviewLabel;
import dev.handsup.review.dto.ReviewMapper;
import dev.handsup.review.dto.request.RegisterReviewRequest;
import dev.handsup.review.dto.response.ReviewResponse;
import dev.handsup.review.repository.ReviewInterReviewLabelRepository;
import dev.handsup.review.repository.ReviewLabelRepository;
import dev.handsup.review.repository.ReviewRepository;
import dev.handsup.user.domain.User;
import dev.handsup.user.repository.UserReviewLabelRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewLabelRepository reviewLabelRepository;
	private final ReviewInterReviewLabelRepository reviewInterReviewLabelRepository;
	private final UserReviewLabelRepository userReviewLabelRepository;
	private final AuctionRepository auctionRepository;
	private final EntityManagementService entityManagementService;

	@Transactional
	public ReviewResponse registerReview(
		RegisterReviewRequest request,
		Long auctionId,
		User writer
	) {
		Auction auction = entityManagementService.getEntity(auctionRepository, auctionId);
		Review review = reviewRepository.save(
			ReviewMapper.toReview(request, auction, writer)
		);
		request.reviewLabelIds().forEach(reviewLabelId -> {
			ReviewLabel reviewLabel = entityManagementService.getEntity(
				reviewLabelRepository,
				reviewLabelId
			);
			reviewInterReviewLabelRepository.save(
				ReviewInterReviewLabel.of(review, reviewLabel)
			);

			// UserReviewLabel 카운팅
			UserReviewLabel userReviewLabel = userReviewLabelRepository.save(
				UserReviewLabel.of(reviewLabel, auction.getSeller())
			);
			Long userReviewLabelId = userReviewLabel.getId();
			int userReviewLabelCount = entityManagementService.getEntity(
				userReviewLabelRepository, userReviewLabelId
			).getCount();
			userReviewLabelRepository.updateCount(userReviewLabelId, userReviewLabelCount + 1);
		});

		return ReviewMapper.toReviewResponse(review);
	}

	@Transactional(readOnly = true)
	public PageResponse<ReviewResponse> getReviewsOfAuction(Long auctionId, Pageable pageable) {
		Slice<ReviewResponse> reviewResponsePage = reviewRepository
			.findByAuctionIdOrderByCreatedAtDesc(auctionId, pageable)
			.map(ReviewMapper::toReviewResponse);
		return CommonMapper.toPageResponse(reviewResponsePage);
	}
}
