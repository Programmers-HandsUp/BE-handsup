package dev.handsup.bidding.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BiddingErrorCode implements ErrorCode {
	NOT_FOUND_BIDDING("해당 아이디의 입찰이 존재하지 않습니다.", "B_000"),
	BIDDING_PRICE_LESS_THAN_INIT_PRICE("입찰가는 최소 입찰가보다 높아야 합니다.", "B_001"),
	BIDDING_PRICE_NOT_HIGH_ENOUGH("입찰가는 최고 입찰가보다 100원 이상 높아야 합니다.", "B_002"),
	CAN_NOT_COMPLETE_TRADING("거래를 완료할 수 없는 상태입니다.", "B_003"),
	CAN_NOT_CANCEL_TRADING("거래를 취소할 수 없는 상태입니다.", "B_004"),
	CAN_NOT_PREPARE_TRADING("거래를 준비할 수 없는 상태입니다.", "B_005"),

	CAN_NOT_PROGRESS_TRADING("거래를 진행할 수 없는 상태입니다.", "B_006"),
	NOT_AUTHORIZED_SELLER("거래 상태를 판매자만 변경 가능합니다.", "B_007"),
	NOT_ALLOW_SELF_BIDDING("판매자는 자신의 경매에 입찰을 할 수 없습니다.", "B_008"),
	NOT_FOUND_BIDDING_BY_AUCTION_AND_BIDDER(
		"경매 아이디와 입찰자 아이디에 해당하는 입찰이 존재하지 않습니다.", "B_009"),
  NOT_FOUND_NEXT_BIDDING("다음 아이디의 입찰이 존재하지 않습니다.", "B_010");

	private final String message;
	private final String code;
}
