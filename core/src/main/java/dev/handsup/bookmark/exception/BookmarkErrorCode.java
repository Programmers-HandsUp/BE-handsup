package dev.handsup.bookmark.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookmarkErrorCode implements ErrorCode {

	NOT_FOUND_BOOKMARK("이미 북마크를 취소하였습니다.", "BM_001"),
	ALREADY_EXISTS_BOOKMARK("이미 북마크에 추가하였습니다.", "BM_002");

	private final String message;
	private final String code;
}