package dev.handsup.bidding.dto;

import static lombok.AccessLevel.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(access = PRIVATE)
public record RegisterBiddingApiRequest(

	@NotBlank(message = "biddingPrice 값이 공백입니다.")
	@Max(value = 1_000_000_000, message = "최대 입찰가는 10억입니다.")
	int biddingPrice
) { }
