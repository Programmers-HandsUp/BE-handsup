package dev.handsup.auction.dto.response;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record AuctionDetailResponse(

	Long auctionId,
	String title,

	String productCategory,

	int initPrice,

	LocalDate endDate,

	String productStatus,

	String purchaseTime,

	String description,

	String tradeMethod,

	String si,
	String gu,
	String dong
) {
}
