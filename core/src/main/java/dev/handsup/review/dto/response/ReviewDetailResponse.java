package dev.handsup.review.dto.response;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.auction_field.TradingLocation;
import lombok.Builder;

@Builder(access = PRIVATE)
public record ReviewDetailResponse(

	int evaluationScore,
	String content,
	Long auctionId,
	Long writerId,
	String auctionTitle,
	int winningPrice,
	TradeMethod tradeMethod,
	TradingLocation tradingLocation,
	String tradingCreatedAt,
	String reviewCreatedAt
) {
	public static ReviewDetailResponse of(
		int evaluationScore,
		String content,
		Long auctionId,
		Long writerId,
		String auctionTitle,
		int winningPrice,
		TradeMethod tradeMethod,
		TradingLocation tradingLocation,
		String tradingCreatedAt,
		String reviewCreatedAt
	) {
		return ReviewDetailResponse.builder()
			.evaluationScore(evaluationScore)
			.content(content)
			.auctionId(auctionId)
			.writerId(writerId)
			.auctionTitle(auctionTitle)
			.winningPrice(winningPrice)
			.tradeMethod(tradeMethod)
			.tradingLocation(tradingLocation)
			.tradingCreatedAt(tradingCreatedAt)
			.reviewCreatedAt(reviewCreatedAt)
			.build();
	}
}
