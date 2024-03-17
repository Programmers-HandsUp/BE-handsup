package dev.handsup.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

	CHAT("채팅 알림", "님이 회원님과의 채팅방에서 속삭이고 있어요."),
	COMMENT("댓글 알림", "님이 회원님의 경매 물품에서 얘기하고 있어요."),
	BOOKMARK("북마크 알림", "님이 회원님의 경매 물품을 관심있어 해요."),
	PURCHASE_WINNING("구매 입찰의 낙찰 알림", "입찰하신 물품이 낙찰되었습니다."),
	CANCELED_PURCHASE_WINNING("구매 입찰의 거래 취소 알림", "거래가 취소되었습니다."),
	COMPLETED_PURCHASE_WINNING("구매 입찰의 거래 완료 알림", "거래가 완료되었습니다.");

	private final String title;
	private final String content;
}
