package dev.handsup.auction.dto.request;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record RegisterAuctionRequest(

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
