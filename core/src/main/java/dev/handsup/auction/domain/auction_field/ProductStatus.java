package dev.handsup.auction.domain.auction_field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {

	NEW("미개봉"),
	CLEAN("깨끗함"),
	DIRTY("더러움");

	private final String description;
}
