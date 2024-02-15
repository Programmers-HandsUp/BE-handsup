package dev.handsup.domain.auction.auction_category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategoryValue {
	DIGITAL("디지털 기기"),
	FURNITURE("가구"),
	FASHION("패션");

	private final String description;
}
