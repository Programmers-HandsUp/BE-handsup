package dev.handsup.bidding.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.dto.request.RegisterBiddingRequest;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class BiddingApiMapper {

	public static RegisterBiddingRequest toRegisterBiddingRequest(
		RegisterBiddingApiRequest request,
		Auction auction,
		User bidder
	) {
		return RegisterBiddingRequest.of(
			request.biddingPrice(),
			auction,
			bidder
		);
	}
}
