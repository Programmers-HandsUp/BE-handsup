package dev.handsup.search.domain;

import static dev.handsup.auction.exception.AuctionErrorCode.*;

import java.util.Arrays;

import dev.handsup.common.exception.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum KeywordType {
	POPULAR("search:popular"),
	RECENT("search:recent");
	private final String key;

	public static KeywordType of(String input) {
		return Arrays.stream(values())
			.filter(type -> type.isEqual(input))
			.findAny()
			.orElseThrow(() -> new ValidationException(NOT_FOUND_PRODUCT_STATUS));
	}

	private boolean isEqual(String input) {
		return input.equalsIgnoreCase(this.key);
	}
}
