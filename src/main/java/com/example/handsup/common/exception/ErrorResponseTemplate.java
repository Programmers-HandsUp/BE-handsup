package com.example.handsup.common.exception;

public record ErrorResponseTemplate(
	String message,
	String code
) {
}
