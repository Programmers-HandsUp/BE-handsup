package com.core.domain.auction.history;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import com.core.common.TimeBaseEntity;
import com.core.domain.auction.Auction;
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
public class BiddingHistory extends TimeBaseEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "bidding_history_id")
	private Long id;

	@Column(name = "bidding_price")
	private int biddingPrice;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "auction_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Auction auction;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "bidder_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User bidder;

	@Builder
	public BiddingHistory(int biddingPrice, Auction auction, User bidder) {
		this.biddingPrice = biddingPrice;
		this.auction = auction;
		this.bidder = bidder;
	}
}
