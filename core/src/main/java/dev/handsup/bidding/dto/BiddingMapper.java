package dev.handsup.bidding.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.bidding.dto.response.RegisterBiddingResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class BiddingMapper {

	public static RegisterBiddingResponse toRegisterBiddingResponse(Bidding bidding) {
		return RegisterBiddingResponse.of(
			bidding.getBiddingPrice(),
			bidding.getAuction().getId(),
			bidding.getBidder().getId()
		);
	}

	public static Bidding toBidding(
		RegisterBiddingRequest request,
		Auction auction,
		User bidder
	) {
		return Bidding.of(
			request.biddingPrice(),
			auction,
			bidder
		);
	}
}
