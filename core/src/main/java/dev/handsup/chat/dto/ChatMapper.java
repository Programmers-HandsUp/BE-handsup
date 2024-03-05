package dev.handsup.chat.dto;

import static lombok.AccessLevel.*;

import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChatMapper {
	public static ChatRoom toChatRoom(Long auctionId, User seller, User bidder) {
		return ChatRoom.of(auctionId, seller, bidder);
	}

	public static RegisterChatRoomResponse toRegisterChatRoomResponse(ChatRoom chatRoom) {
		return RegisterChatRoomResponse.of(chatRoom.getId());
	}

	public static ChatRoomSimpleResponse toChatRoomSimpleResponse(ChatRoom chatRoom, User receiver) {
		return ChatRoomSimpleResponse.of(chatRoom.getId(), receiver.getNickname(), receiver.getProfileImageUrl());
	}
}
