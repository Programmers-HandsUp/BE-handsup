package dev.handsup.user.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	NOT_FOUND_USER("해당 아이디의 유저가 존재하지 않습니다.", "U_000"),
	NOT_FOUND_BY_EMAIL("EMAIL 에 해당하는 사용자가 존재하지 않습니다", "U_001"),
	DUPLICATED_EMAIL("이미 존재하는 이메일입니다. 회원가입을 다시 진행해주세요.", "U_002"),
	NON_VALIDATED_EMAIL("이메일 주소 양식을 확인해주세요.", "U_003");

	private final String message;
	private final String code;
}
