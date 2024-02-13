package com.core.domain.chat;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.core.common.TimeBaseEntity;
import com.core.domain.user.User;

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

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "seller_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User seller;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "buyer_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User buyer;

	@Column(name = "is_seller_confirmed")
	private Boolean isSellerConfirmed;

	@Column(name = "is_buyer_confirmed")
	private Boolean isBuyerConfirmed;

	@Builder
	public ChatRoom(User seller, User buyer) {
		this.seller = seller;
		this.buyer = buyer;
		isSellerConfirmed = false;
		isBuyerConfirmed = false;
	}
}
