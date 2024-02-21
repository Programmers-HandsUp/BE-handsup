package dev.handsup.bidding.dto.response;

import static lombok.AccessLevel.*;

import lombok.Builder;

@Builder(access = PRIVATE)
public record RegisterBiddingResponse(
	Long auctionId
) {
	public static RegisterBiddingResponse from(Long auctionId) {
		return RegisterBiddingResponse.builder()
			.auctionId(auctionId)
			.build();
	}
}
