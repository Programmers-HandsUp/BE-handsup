package dev.handsup.bookmark.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BookmarkException extends RuntimeException {

	private final String code;

	public BookmarkException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}
}
