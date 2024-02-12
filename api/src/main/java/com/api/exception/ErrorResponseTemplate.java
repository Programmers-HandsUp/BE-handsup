package com.api.exception;

public record ErrorResponseTemplate(
	String message,
	String code
) {
}
