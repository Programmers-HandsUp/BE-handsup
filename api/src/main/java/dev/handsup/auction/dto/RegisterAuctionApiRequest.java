package dev.handsup.auction.dto;

import java.time.LocalDate;

public record RegisterAuctionApiRequest(
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
){}
