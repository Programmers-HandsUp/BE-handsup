package com.example.handsup.domain.auction.auction_field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuctionStatus {
	PROGRESS, TRADING, COMPLETED
}
