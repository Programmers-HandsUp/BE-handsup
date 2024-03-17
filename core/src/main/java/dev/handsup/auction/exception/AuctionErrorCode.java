package dev.handsup.auction.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuctionErrorCode implements ErrorCode {

	NOT_FOUND_AUCTION("해당 아이디의 경매가 존재하지 않습니다.", "AU_000"),
	NOT_FOUND_PRODUCT_STATUS("상품 상태를 올바르게 입력해주세요.", "AU_001"),
	NOT_FOUND_PURCHASE_TIME("구매 시기를 올바르게 입력해주세요.", "AU_002"),
	NOT_FOUND_TADE_METHOD("거래 방법을 올바르게 입력해주세요.", "AU_003"),
	NOT_FOUND_PRODUCT_CATEGORY("상품 카테고리를 올바르게 입력해주세요.", "AU_004"),
	INVALID_SORT_INPUT("정렬 기준을 올바르게 입력해주세요.", "AU_005"),
	EMPTY_SORT_INPUT("정렬 기준을 입력해주세요.", "AU_006"),
	CAN_NOT_COMPLETE_AUCTION("경매를 완료 상태로 변경할 수 없습니다.", "AU_007");

	private final String message;
	private final String code;
}
