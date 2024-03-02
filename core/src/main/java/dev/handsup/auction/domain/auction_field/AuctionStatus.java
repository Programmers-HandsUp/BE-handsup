package dev.handsup.auction.domain.auction_field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuctionStatus {

	PROGRESS("입찰중"),
	TRADING("거래중"),
	COMPLETED("종료"),
	CANCELED("취소");

	private final String label;
}
