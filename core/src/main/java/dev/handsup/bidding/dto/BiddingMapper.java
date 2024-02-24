package dev.handsup.bidding.dto;

import static lombok.AccessLevel.*;

import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.dto.response.BiddingResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class BiddingMapper {

	public static BiddingResponse toRegisterBiddingResponse(Bidding bidding) {
		return BiddingResponse.of(
			bidding.getBiddingPrice(),
			bidding.getAuction().getId(),
			bidding.getBidder().getNickname()
		);
	}
}
