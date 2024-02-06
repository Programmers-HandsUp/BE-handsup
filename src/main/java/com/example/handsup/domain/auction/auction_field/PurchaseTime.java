package com.example.handsup.domain.auction.auction_field;

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
}
