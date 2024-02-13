package com.handsup.exception;

public record ErrorResponseTemplate(
	String message,
	String code
) {
}
