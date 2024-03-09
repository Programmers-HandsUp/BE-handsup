package dev.handsup.bidding.dto.response;

import static lombok.AccessLevel.*;

import lombok.Builder;

@Builder(access = PRIVATE)
public record BiddingResponse(
	int biddingPrice,
	Long auctionId,
	Long bidderId,
	String bidderNickname,
	String tradingStatus,
	String imgUrl,
	String createdAt
) {
	public static BiddingResponse of(
		int biddingPrice,
		Long auctionId,
		Long bidderId,
		String bidderNickname,
		String tradingStatus,
		String imgUrl,
		String createdAt
	) {
		return BiddingResponse.builder()
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
