package dev.handsup.bidding.dto.request;

import static lombok.AccessLevel.*;

import dev.handsup.user.domain.User;
import lombok.Builder;

@Builder(access = PRIVATE)
public record RegisterBiddingRequest(

	int biddingPrice,
	Long auctionId,
	User bidder
) {
	public static RegisterBiddingRequest of(int biddingPrice, Long auctionId, User bidder) {
		return RegisterBiddingRequest.builder()
			.biddingPrice(biddingPrice)
			.auctionId(auctionId)
			.bidder(bidder)
			.build();
	}
}
