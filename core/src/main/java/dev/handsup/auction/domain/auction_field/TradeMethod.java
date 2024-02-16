package dev.handsup.auction.domain.auction_field;

import static dev.handsup.auction.exception.AuctionErrorCode.*;

import java.util.Arrays;

import dev.handsup.common.exception.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeMethod {

	DIRECT("직거래"),
	DELIVER("택배");

	private final String description;

	public static TradeMethod of(String input){
		return Arrays.stream(values())
			.filter(method -> method.isEqual(input))
			.findAny()
			.orElseThrow(() -> new ValidationException(NOT_FOUND_PRODUCT_STATUS));
	}

	private boolean isEqual(String input) {
		return input.equalsIgnoreCase(this.description);
	}
}
