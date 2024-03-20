package dev.handsup.auction.domain.auction_field;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuctionStatus {

	BIDDING("입찰 중"),
	TRADING("거래 중"),
	COMPLETED("거래 완료"),
	CANCELED("취소");

	private final String label;
}
