package dev.handsup.chat.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {

	CHAT_ROOM_ALREADY_EXISTS("채팅방이 이미 존재합니다.", "CR_001"),
	NOT_TRADING_AUCTION("거래 상태의 경매가 아닙니다.", "CR_002");
	private final String message;
	private final String code;
}
