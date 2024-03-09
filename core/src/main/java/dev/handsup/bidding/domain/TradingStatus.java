package dev.handsup.bidding.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TradingStatus {
	WAITING("대기중"),
	PREPARING("준비중"),
	CHATTING("채팅중"),
	CANCELED("취소됨"),
	COMPLETED("완료됨");

	private final String label;
}
