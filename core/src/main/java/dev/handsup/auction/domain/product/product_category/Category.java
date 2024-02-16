package dev.handsup.auction.domain.product.product_category;

import static dev.handsup.auction.exception.AuctionErrorCode.*;

import java.util.Arrays;

import dev.handsup.common.exception.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

	DIGITAL("디지털 기기"),
	FURNITURE("가구"),
	FASHION("패션");

	private final String description;

	public static Category of(String input){
		return Arrays.stream(values())
			.filter(value -> value.isEqual(input))
			.findAny()
			.orElseThrow(() -> new ValidationException(NOT_FOUND_PRODUCT_STATUS));
	}

	private boolean isEqual(String input) {
		return input.equalsIgnoreCase(this.description);
	}
}
