package dev.handsup.chat.dto.response;

public record ChatRoomSimpleResponse(
	Long chatRoomId,
	String receiverNickName,
	String receiverImageUrl
) {
	public static ChatRoomSimpleResponse of(Long chatRoomId, String receiverNickName, String receiverImageUrl) {
		return new ChatRoomSimpleResponse(chatRoomId, receiverNickName, receiverImageUrl);
	}
}
