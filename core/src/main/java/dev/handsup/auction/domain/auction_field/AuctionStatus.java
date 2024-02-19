package dev.handsup.auction.domain.auction_field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuctionStatus {

	PROGRESS("경매중"),
	TRADING("거래중"),
	COMPLETED("완료"),
	CANCELED("취소");

	private final String label;
}
