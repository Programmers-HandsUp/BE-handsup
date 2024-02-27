package dev.handsup.auth.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	FAILED_LOGIN_BY_ANYTHING("등록되지 않은 아이디이거나 아이디 또는 비밀번호를 잘못 입력했습니다.", "AUTH_001"),
	NOT_PERMITTED_USER("해당 요청에 대한 권한이 없습니다.", "AUTH_002"),
	INVALID_TOKEN_ETC("기타 보안 문제로 토큰이 유효하지 못합니다.", "AUTH_003"),
	NOT_FOUND_REFRESH_TOKEN("해당 리프레쉬 토큰의 인증 데이터가 존재하지 않습니다.", "AUTH_004"),
	NOT_FOUND_REFRESH_TOKEN_IN_COOKIES("요청 쿠키에 리스레쉬 토큰이 존재하지 않습니다.", "AUTH_005"),
	NOT_FOUND_ACCESS_TOKEN_IN_REQUEST("요청에 액세스 토큰이 존재하지 않습니다.", "AUTH_006"),
	NOT_FOUND_REQUEST("해당 요청이 존재하지 않습니다.", "AUTH_007"),
	NOT_FOUND_USER_ID("해당 유저의 인증 데이터가 존재하지 않습니다.", "AUTH_008"),
	TOKEN_EXPIRED("토큰이 만료 시간을 초과했습니다.", "AUTH_009"),
	UNSUPPORTED_TOKEN("토큰 유형이 지원되지 않습니다.", "AUTH_010"),
	MALFORMED_TOKEN("토큰의 구조가 올바르지 않습니다.", "AUTH_011"),
	BLACKLISTED_TOKEN("해당 토큰은 블랙리스트에 등록되어있으므로 유효하지 않습니다.", "AUTH_012"),
	NOT_FOUND_REFRESH_TOKEN_IN_RESPONSE("응답에 리프레쉬 토큰이 존재하지 않습니다.", "AUTH_013"),
	NOT_FOUND_BEARER_IN_REQUEST_ACCESS_TOKEN("요청의 액세스 토큰에 Bearer Type 이 존재하지 않습니다.", "AUTH_014");

	private final String message;
	private final String code;
}
