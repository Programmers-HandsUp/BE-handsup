package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChatRoomFixture {
	public static ChatRoom chatRoom(Long auctionId, User seller, User buyer) {
		return ChatRoom.of(
			auctionId,
			seller,
			buyer
		);
	}
}
