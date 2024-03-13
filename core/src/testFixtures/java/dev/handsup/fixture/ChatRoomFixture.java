package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.bidding.domain.Bidding;
import dev.handsup.chat.domain.ChatRoom;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChatRoomFixture {

	public static ChatRoom chatRoom(Bidding bidding) {
		return ChatRoom.of(
			bidding.getAuction().getId(),
			bidding.getAuction().getSeller(),
			bidding.getBidder(),
			bidding.getId()
		);
	}

	public static ChatRoom chatRoom(User seller, Bidding bidding) {
		return ChatRoom.of(
			bidding.getAuction().getId(),
			seller,
			bidding.getBidder(),
			bidding.getId()
		);
	}

	public static ChatRoom chatRoom(Long auctionId, User seller, User bidder, Long biddingId) {
		return ChatRoom.of(
			auctionId,
			seller,
			bidder,
			biddingId
		);
	}
}
