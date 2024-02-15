package com.api.common.exception;

public record ErrorResponseTemplate(
	String message,
	String code
) {
}
