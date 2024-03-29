package dev.handsup.review.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.exception.BiddingErrorCode;
import dev.handsup.bidding.repository.BiddingRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.review.domain.Review;
import dev.handsup.review.domain.ReviewInterReviewLabel;
import dev.handsup.review.domain.ReviewLabel;
import dev.handsup.review.domain.UserReviewLabel;
import dev.handsup.review.dto.ReviewMapper;
import dev.handsup.review.dto.request.RegisterReviewRequest;
import dev.handsup.review.dto.response.ReviewDetailResponse;
import dev.handsup.review.dto.response.ReviewSimpleResponse;
import dev.handsup.review.exception.ReviewErrorCode;
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
	private final BiddingRepository biddingRepository;

	@Transactional
	public ReviewDetailResponse registerReview(
		RegisterReviewRequest request,
		Long auctionId,
		User writer
	) {
		Auction auction = getAuctionById(auctionId);
		Bidding bidding = getBidding(auction, writer);
		User seller = auction.getSeller();
		Review review = reviewRepository.save(ReviewMapper.toReview(request, auction, writer));

		request.reviewLabelIds().forEach(reviewLabelId -> {
			ReviewLabel reviewLabel = getReviewById(reviewLabelId);
			reviewInterReviewLabelRepository.save(
				ReviewInterReviewLabel.of(review, reviewLabel)
			);

			UserReviewLabel sellerReviewLabel = userReviewLabelRepository.save(
				UserReviewLabel.of(reviewLabel, seller)
			);
			sellerReviewLabel.increaseCount();
		});

		seller.operateScore(request.evaluationScore());

		return ReviewMapper.toReviewDetailResponse(review, auction, bidding);
	}

	private ReviewLabel getReviewById(Long reviewLabelId) {
		return reviewLabelRepository.findById(reviewLabelId)
			.orElseThrow(() -> new NotFoundException(ReviewErrorCode.NOT_FOUND_REVIEW_LABEL));
	}

	public Auction getAuctionById(Long auctionId) {
		return auctionRepository.findById(auctionId).
			orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_AUCTION));
	}

	@Transactional(readOnly = true)
	public PageResponse<ReviewSimpleResponse> getReviewsOfAuction(Long auctionId, Pageable pageable) {
		Slice<ReviewSimpleResponse> reviewResponsePage = reviewRepository
			.findByAuctionIdOrderByCreatedAtDesc(auctionId, pageable)
			.map(ReviewMapper::toReviewSimpleResponse);
		return CommonMapper.toPageResponse(reviewResponsePage);
	}

	private Bidding getBidding(Auction auction, User bidder) {
		return biddingRepository.findByAuctionAndBidder(auction, bidder)
			.orElseThrow(() -> new NotFoundException(BiddingErrorCode.NOT_FOUND_BIDDING_BY_AUCTION_AND_BIDDER));
	}
}
