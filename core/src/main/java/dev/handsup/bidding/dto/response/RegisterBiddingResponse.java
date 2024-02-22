package dev.handsup.bidding.dto.response;

import static lombok.AccessLevel.*;

import lombok.Builder;

@Builder(access = PRIVATE)
public record RegisterBiddingResponse(
	int biddingPrice,
	Long auctionId,
	Long bidderId
) {
	public static RegisterBiddingResponse of(
		int biddingPrice,
		Long auctionId,
		Long bidderId
	) {
		return RegisterBiddingResponse.builder()
			.biddingPrice(biddingPrice)
			.auctionId(auctionId)
			.bidderId(bidderId)
			.build();
	}
}
