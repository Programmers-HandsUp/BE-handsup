package dev.handsup.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
	CHAT("채팅"),
	COMMENT("댓글"),
	BOOKMARK("북마크"),
	BIDDING_SUCCESS("낙찰");
	private final String label;
}
