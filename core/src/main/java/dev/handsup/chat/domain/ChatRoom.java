package dev.handsup.chat.domain;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.handsup.common.entity.TimeBaseEntity;
import dev.handsup.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "chat_room_id")
	private Long id;

	@Column(name = "auction_id")
	private Long auctionId;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "seller_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User seller;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "buyer_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User buyer;

	@Builder
	private ChatRoom(Long auctionId, User seller, User buyer) {
		this.auctionId = auctionId;
		this.seller = seller;
		this.buyer = buyer;
	}

	public static ChatRoom of(Long auctionId, User seller, User buyer) {
		return ChatRoom.builder()
			.auctionId(auctionId)
			.seller(seller)
			.buyer(buyer)
			.build();
	}
}
