package dev.handsup.review.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.review.domain.Review;
import dev.handsup.review.dto.request.RegisterReviewRequest;
import dev.handsup.review.dto.response.ReviewDetailResponse;
import dev.handsup.review.dto.response.ReviewSimpleResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ReviewMapper {

	public static Review toReview(
		RegisterReviewRequest request,
		Auction auction,
		User writer
	) {
		return Review.of(
			request.evaluationScore(),
			request.content(),
			auction,
			writer
		);
	}

	public static ReviewSimpleResponse toReviewSimpleResponse(Review review) {
		return ReviewSimpleResponse.of(
			review.getId(),
			review.getEvaluationScore(),
			review.getContent(),
			review.getAuction().getId(),
			review.getWriter().getId(),
			review.getWriter().getNickname(),
			review.getCreatedAt().toString()
		);
	}

	public static ReviewDetailResponse toReviewDetailResponse(
		Review review, Auction auction, Bidding bidding) {
		return ReviewDetailResponse.of(
			review.getId(),
			review.getEvaluationScore(),
			review.getContent(),
			auction.getId(),
			review.getWriter().getId(),
			review.getWriter().getNickname(),
			auction.getTitle(),
			bidding.getBiddingPrice(),
			auction.getTradeMethod().toString(),
			auction.getTradingLocation(),
			bidding.getUpdatedAt().toString(),
			review.getCreatedAt().toString()
		);
	}

}
