package dev.handsup.bidding.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.dto.response.BiddingResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class BiddingMapper {

	public static BiddingResponse toBiddingResponse(Bidding bidding) {
		return BiddingResponse.of(
			bidding.getId(),
			bidding.getBiddingPrice(),
			bidding.getAuction().getId(),
			bidding.getBidder().getId(),
			bidding.getBidder().getNickname(),
			bidding.getTradingStatus().getLabel(),
			bidding.getBidder().getProfileImageUrl(),
			bidding.getCreatedAt().toString()
		);
	}

	public static Bidding toBidding(int biddingPrice, Auction auction, User bidder) {
		return Bidding.of(
			biddingPrice,
			auction,
			bidder
		);
	}

}
