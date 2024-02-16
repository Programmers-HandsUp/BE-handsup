package dev.handsup.auction.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuctionErrorCode implements ErrorCode {

	NOT_FOUND_PRODUCT_STATUS("올바른 상품 상태를 입력해주세요", "P_001");

	private final String message;
	private final String code;
}
