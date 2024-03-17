package dev.handsup.chat.dto.response;

public record ChatMessageResponse(
	Long chatRoomId,
	Long senderId,
	String content,
	String createdAt
) {
	public static ChatMessageResponse of(
		Long chatRoomId,
		Long senderId,
		String content,
		String createdAt
	) {
		return new ChatMessageResponse(
			chatRoomId, senderId, content, createdAt
		);
	}
}
