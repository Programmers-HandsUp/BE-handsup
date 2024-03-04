package dev.handsup.chat.dto.request;

public record RegisterChatRoomResponse(
	Long chatRoomId
){
	public static RegisterChatRoomResponse of(Long chatRoomId){
		return new RegisterChatRoomResponse(chatRoomId);
	}
}
