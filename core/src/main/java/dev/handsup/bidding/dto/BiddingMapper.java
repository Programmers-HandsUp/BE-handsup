package dev.handsup.bidding.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.dto.response.BiddingResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class BiddingMapper {

	public static BiddingResponse toBiddingResponse(Bidding bidding) {
		return BiddingResponse.of(
			bidding.getBiddingPrice(),
			bidding.getAuction().getId(),
			bidding.getBidder().getId(),
			bidding.getBidder().getNickname(),
			bidding.getStatus().getLabel(),
			bidding.getAuction().getProduct().getImages().get(0).getImageUrl(),
			bidding.getCreatedAt().toString()
		);
	}

	public static Bidding toBidding(RegisterBiddingRequest request, Auction auction, User bidder) {
		return Bidding.of(
			request.biddingPrice(),
			auction,
			bidder
		);
	}
}
