package dev.handsup.chat.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorCode implements ErrorCode {

	CHAT_ROOM_ALREADY_EXISTS("채팅방이 이미 존재합니다.", "CR_001"),
	CHAT_ROOM_ACCESS_DENIED("채팅방 조회 권한이 없습니다.", "CR_003"),
	NOT_FOUND_CHAT_ROOM("해당 아이디의 채팅방이 존재하지 않습니다.", "CR_004"),
	NOT_FOUND_CHAT_ROOM_BY_BIDDING_ID("입찰 아이디에 해당하는 채팅방이 존재하지 않습니다.", "CR_005");

	private final String message;
	private final String code;
}
