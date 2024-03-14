package dev.handsup.bookmark.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookmarkErrorCode implements ErrorCode {

	NOT_FOUND_BOOKMARK("이미 북마크를 취소하였습니다.", "BM_001"),
	ALREADY_EXISTS_BOOKMARK("이미 북마크에 추가하였습니다.", "BM_002"),
	NOT_ALLOW_SELF_BOOKMARK("자신의 경매에는 북마크를 추가할 수 없습니다.", "BM_003");

	private final String message;
	private final String code;
}
