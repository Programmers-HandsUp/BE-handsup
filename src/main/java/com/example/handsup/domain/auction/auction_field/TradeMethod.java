package com.example.handsup.domain.auction.auction_field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeMethod {
	DIRECT("직거래"),
	DELIVER("택배");
	private final String description;
}
