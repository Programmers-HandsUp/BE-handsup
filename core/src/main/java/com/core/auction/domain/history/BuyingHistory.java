package com.core.auction.domain.history;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.core.common.entity.TimeBaseEntity;
import com.core.auction.domain.Auction;
import com.core.user.domain.User;

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
public class BuyingHistory extends TimeBaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "buying_history_id")
	private Long id;

	@Column(name = "bidding_price")
	private int biddingPrice;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "auction_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Auction auction;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "buyer_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User buyer;

	@Builder
	public BuyingHistory(int biddingPrice, Auction auction, User buyer) {
		this.biddingPrice = biddingPrice;
		this.auction = auction;
		this.buyer = buyer;
	}
}
