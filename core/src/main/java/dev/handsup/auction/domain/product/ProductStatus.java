package dev.handsup.auction.domain.product;

import static dev.handsup.auction.exception.AuctionErrorCode.*;

import java.util.Arrays;

import dev.handsup.common.exception.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {

	NEW("미개봉"),
	CLEAN("깨끗해요"),
	DIRTY("사용감 있음");

	private final String label;

	public static ProductStatus of(String input) {
		return Arrays.stream(values())
			.filter(status -> status.isEqual(input))
			.findAny()
			.orElseThrow(() -> new ValidationException(NOT_FOUND_PRODUCT_STATUS));
	}

	private boolean isEqual(String input) {
		return input.equals(this.label);
	}
}
