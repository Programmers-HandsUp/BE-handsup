package dev.handsup.bidding.dto.request;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.user.domain.User;
import lombok.Builder;

@Builder(access = PRIVATE)
public record RegisterBiddingRequest(

	int biddingPrice,
	Auction auction,
	User bidder
) {
	public static RegisterBiddingRequest of(int biddingPrice, Auction auction, User bidder) {
		return RegisterBiddingRequest.builder()
			.biddingPrice(biddingPrice)
			.auction(auction)
			.bidder(bidder)
			.build();
	}
}
