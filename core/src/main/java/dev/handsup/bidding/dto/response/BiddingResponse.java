package dev.handsup.bidding.dto.response;

import static lombok.AccessLevel.*;

import lombok.Builder;

@Builder(access = PRIVATE)
public record BiddingResponse(
	int biddingPrice,
	Long auctionId,
	String bidderNickname
) {
	public static BiddingResponse of(
		int biddingPrice,
		Long auctionId,
		String bidderNickname
	) {
		return BiddingResponse.builder()
			.biddingPrice(biddingPrice)
			.auctionId(auctionId)
			.bidderNickname(bidderNickname)
			.build();
	}
}
