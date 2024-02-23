package dev.handsup.bidding.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BiddingErrorCode implements ErrorCode {

	BIDDING_PRICE_LESS_THAN_INIT_PRICE("입찰가는 최소 입찰가보다 높아야 합니다.", "B_001"),
	BIDDING_PRICE_NOT_HIGH_ENOUGH("입찰가는 최고 입찰가보다 100원 이상 높아야 합니다.", "B_002");

	private final String message;
	private final String code;
}
