package dev.handsup.chat.dto;

import static lombok.AccessLevel.*;

import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.request.RegisterChatRoomResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChatMapper {
	public static ChatRoom toChatRoom(User seller, User buyer){
		return ChatRoom.of(seller, buyer);
	}

	public static RegisterChatRoomResponse toRegisterChatRoomResponse(ChatRoom chatRoom){
		return RegisterChatRoomResponse.of(chatRoom.getId());
	}
}
