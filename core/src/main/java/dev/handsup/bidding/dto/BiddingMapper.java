package dev.handsup.bidding.dto;

import static lombok.AccessLevel.*;

import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.dto.response.RegisterBiddingResponse;
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
}
