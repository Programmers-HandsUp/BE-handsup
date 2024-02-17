package dev.handsup.auction.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record AuctionResponse(

	Long auctionId,

	String title,

	String category,

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
