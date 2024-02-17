package dev.handsup.auction.domain.auction_field;

import static dev.handsup.auction.exception.AuctionErrorCode.*;

import java.util.Arrays;

import dev.handsup.common.exception.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseTime {

	UNDER_ONE_MONTH("1개월 이하"),
	UNDER_THREE_MONTH("3개월 이하"),
	UNDER_SIX_MONTH("6개월 이하"),
	UNDER_ONE_YEAR("1년 이하"),
	ABOVE_ONE_YEAR("1년 이상"),
	UNKNOWN("모름");

	private final String description;

	public static PurchaseTime of(String input){
		return Arrays.stream(values())
			.filter(time -> time.isEqual(input))
			.findAny()
			.orElseThrow(() -> new ValidationException(NOT_FOUND_PURCHASE_TIME));
	}

	private boolean isEqual(String input) {
		return input.equalsIgnoreCase(this.description);
	}
}
