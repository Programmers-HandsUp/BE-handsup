package dev.handsup.bidding.dto;

import static lombok.AccessLevel.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(access = PRIVATE)
public record RegisterBiddingApiRequest(

	@NotNull(message = "biddingPrice 값이 공백입니다.")
	@Max(value = 1_000_000_000, message = "최대 입찰가는 10억입니다.")
	int biddingPrice
) {
	public static RegisterBiddingApiRequest from(int biddingPrice) {
		return RegisterBiddingApiRequest.builder()
			.biddingPrice(biddingPrice)
			.build();
	}
}
