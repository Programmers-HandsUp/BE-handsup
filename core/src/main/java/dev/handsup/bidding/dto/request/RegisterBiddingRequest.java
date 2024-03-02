package dev.handsup.bidding.dto.request;

import static lombok.AccessLevel.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(access = PRIVATE)
public record RegisterBiddingRequest(

	@NotNull(message = "biddingPrice 은 필수입니다.")
	@Max(value = 1_000_000_000, message = "최대 입찰가는 10억입니다.")
	int biddingPrice
) {
	public static RegisterBiddingRequest from(int biddingPrice) {
		return RegisterBiddingRequest.builder()
			.biddingPrice(biddingPrice)
			.build();
	}
}
