package dev.handsup.auction.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuctionSearchCondition(

	@NotBlank(message = "검색어를 입력해주세요.")
	String keyword,
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