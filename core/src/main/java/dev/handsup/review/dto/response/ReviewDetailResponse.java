package dev.handsup.review.dto.response;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.auction_field.TradingLocation;
import lombok.Builder;

@Builder(access = PRIVATE)
public record ReviewDetailResponse(

	Long reviewId,
	int evaluationScore,
	String content,
	Long auctionId,
	Long writerId,
	String writerNickname,
	String auctionTitle,
	int winningPrice,
	String tradeMethod,
	TradingLocation tradingLocation,
	String tradingCreatedAt,
	String reviewCreatedAt
) {
	public static ReviewDetailResponse of(
		Long reviewId,
		int evaluationScore,
		String content,
		Long auctionId,
		Long writerId,
		String writerNickname,
		String auctionTitle,
		int winningPrice,
		String tradeMethod,
		TradingLocation tradingLocation,
		String tradingCreatedAt,
		String reviewCreatedAt
	) {
		return ReviewDetailResponse.builder()
			.reviewId(reviewId)
			.evaluationScore(evaluationScore)
			.content(content)
			.auctionId(auctionId)
			.writerId(writerId)
			.writerNickname(writerNickname)
			.auctionTitle(auctionTitle)
			.winningPrice(winningPrice)
			.tradeMethod(tradeMethod)
			.tradingLocation(tradingLocation)
			.tradingCreatedAt(tradingCreatedAt)
			.reviewCreatedAt(reviewCreatedAt)
			.build();
	}
}
