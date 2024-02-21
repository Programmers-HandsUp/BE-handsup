package dev.handsup.auction.dto.request;

import lombok.Builder;

@Builder
public record AuctionSearchCondition(
	String productCategory,
	String tradeMethod,
	Boolean isNewProduct,
	Boolean isProgress,
	String si,
	String gu,
	String dong,
	Integer minPrice,
	Integer maxPrice
) {
}