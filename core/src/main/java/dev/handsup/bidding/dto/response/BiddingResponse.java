package dev.handsup.bidding.dto.response;

import static lombok.AccessLevel.*;

import lombok.Builder;

@Builder(access = PRIVATE)
public record BiddingResponse(
	Long biddingId,
	int biddingPrice,
	Long auctionId,
	Long bidderId,
	String bidderNickname,
	String tradingStatus,
	String imgUrl,
	String createdAt
) {
	public static BiddingResponse of(
		Long biddingId,
		int biddingPrice,
		Long auctionId,
		Long bidderId,
		String bidderNickname,
		String tradingStatus,
		String imgUrl,
		String createdAt
	) {
		return BiddingResponse.builder()
			.biddingId(biddingId)
			.biddingPrice(biddingPrice)
			.auctionId(auctionId)
			.bidderId(bidderId)
			.bidderNickname(bidderNickname)
			.tradingStatus(tradingStatus)
			.imgUrl(imgUrl)
			.createdAt(createdAt)
			.build();
	}
}
