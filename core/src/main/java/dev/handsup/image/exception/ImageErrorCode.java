package dev.handsup.image.exception;

import dev.handsup.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {

	EMPTY_FILE_NAME("원본 파일명은 필수입니다.", "IMG_001"),
	INVALID_FILE_EXTENSION("잘못된 파일 형식입니다.", "IMG_002"),
	FAILED_TO_UPLOAD("s3에 이미지를 업로드하는데 실패했습니다.", "IMG_003"),
	FAILED_TO_REMOVE("s3에서 이미지를 제거하는데 실패했습니다.", "IMG_004");

	private final String message;
	private final String code;
}
