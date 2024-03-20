package dev.handsup.chat.dto;

import static lombok.AccessLevel.*;

import dev.handsup.bidding.domain.Bidding;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.response.ChatRoomDetailResponse;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChatRoomMapper {

	public static ChatRoom toChatRoom(Bidding bidding) {
		return ChatRoom.of(
			bidding.getAuction().getId(),
			bidding.getAuction().getSeller(),
			bidding.getBidder(),
			bidding.getId()
		);
	}

	public static RegisterChatRoomResponse toRegisterChatRoomResponse(ChatRoom chatRoom) {
		return RegisterChatRoomResponse.of(chatRoom.getId());
	}

	public static ChatRoomSimpleResponse toChatRoomSimpleResponse(ChatRoom chatRoom, User receiver) {
		return ChatRoomSimpleResponse.of(chatRoom.getId(), receiver.getNickname(), receiver.getProfileImageUrl());
	}

	public static ChatRoomDetailResponse toChatRoomDetailResponse(ChatRoom chatRoom, Bidding bidding, User receiver) {
		return ChatRoomDetailResponse.of(
			chatRoom.getId(),
			bidding.getAuction().getId(),
			bidding.getId(),
			bidding.getBiddingPrice(),
			bidding.getTradingStatus().getLabel(),
			bidding.getAuction().getTitle(),
			bidding.getAuction().getProduct().getImages().get(0).getImageUrl(),
			receiver.getId(),
			receiver.getNickname(),
			receiver.getScore(),
			receiver.getProfileImageUrl());
	}
}
