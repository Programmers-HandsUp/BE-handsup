package dev.handsup.review.service;

import org.springframework.stereotype.Service;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.service.AuctionService;
import dev.handsup.common.service.EntityManagementService;
import dev.handsup.review.domain.Review;
import dev.handsup.review.domain.ReviewInterReviewLabel;
import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.dto.ReviewMapper;
import dev.handsup.review.dto.request.RegisterReviewRequest;
import dev.handsup.review.dto.response.ReviewResponse;
import dev.handsup.review.repository.ReviewInterReviewLabelRepository;
import dev.handsup.review.repository.ReviewLabelRepository;
import dev.handsup.review.repository.ReviewRepository;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewLabelRepository reviewLabelRepository;
	private final ReviewInterReviewLabelRepository reviewInterReviewLabelRepository;
	private final AuctionService auctionService;
	private final EntityManagementService entityManagementService;

	public ReviewResponse registerReview(
		RegisterReviewRequest request,
		Long auctionId,
		User writer
	) {
		Auction auction = auctionService.getAuctionEntity(auctionId);
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
		});

		return ReviewMapper.toReviewResponse(review);
	}
}
