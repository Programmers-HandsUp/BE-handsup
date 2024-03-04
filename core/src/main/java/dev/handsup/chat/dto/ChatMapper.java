package dev.handsup.chat.dto;

import static lombok.AccessLevel.*;

import org.springframework.data.domain.Slice;

import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChatMapper {
	public static ChatRoom toChatRoom(Long auctionId, User seller, User buyer){
		return ChatRoom.of(auctionId, seller, buyer);
	}

	public static RegisterChatRoomResponse toRegisterChatRoomResponse(ChatRoom chatRoom){
		return RegisterChatRoomResponse.of(chatRoom.getId());
	}

	public static <T> PageResponse<T> toPageResponse(Slice<T> page) {
		return PageResponse.of(page);
	}

	public static ChatRoomSimpleResponse toChatRoomSimpleResponse(ChatRoom chatRoom, User receiver) {
		return ChatRoomSimpleResponse.of(chatRoom.getId(), receiver.getNickname(), receiver.getProfileImageUrl());
	}
}
