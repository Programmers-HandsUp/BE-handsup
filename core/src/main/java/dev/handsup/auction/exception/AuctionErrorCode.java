package dev.handsup.auction.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuctionErrorCode implements ErrorCode {

	NOT_FOUND_PRODUCT_STATUS("올바른 상품 상태를 입력해주세요", "P_001"),
	NOT_FOUND_PURCHASE_TIME("올바른 구매 시기를 입력해주세요", "P_002"),
	NOT_FOUND_TADE_METHOD("올바른 거래 방법을 입력해주세요", "P_003"),
	NOT_FOUND_PRODUCT_CATEGORY("DB에 해당 상품 카테고리가 존재하지 않습니다.", "P_005");

	private final String message;
	private final String code;
}
