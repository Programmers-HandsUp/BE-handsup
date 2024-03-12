package dev.handsup.chat.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.dto.response.ChatRoomExistenceResponse;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.chat.dto.response.ChatRoomDetailResponse;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChatRoomMapper {

	// public static ChatRoom toChatRoom(Long auctionId, User seller, User bidder) {
	// 	return ChatRoom.of(auctionId, seller, bidder);
	// }

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

	public static ChatRoomDetailResponse toChatRoomDetailResponse(ChatRoom chatRoom, Auction auction, User bidder) {
		return ChatRoomDetailResponse.of(chatRoom.getId(), auction.getId(), auction.getTitle(), bidder.getId(),
			bidder.getNickname(), bidder.getScore(), bidder.getProfileImageUrl());
	}

	public static ChatRoomExistenceResponse toChatRoomExistenceResponse(Boolean isExist) {
		return ChatRoomExistenceResponse.from(isExist);
	}
}
