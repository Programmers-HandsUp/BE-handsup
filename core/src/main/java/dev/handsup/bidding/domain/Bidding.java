package dev.handsup.bidding.domain;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.exception.BiddingErrorCode;
import dev.handsup.common.entity.TimeBaseEntity;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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
public class Bidding extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "bidding_id")
	private Long id;

	@Column(name = "bidding_price", nullable = false)
	private int biddingPrice;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "auction_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Auction auction;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "bidder_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User bidder;

	@Enumerated(STRING)
	@Column(name = "trading_status", nullable = false)
	private TradingStatus tradingStatus;

	@Builder
	private Bidding(int biddingPrice, Auction auction, User bidder) {
		this.biddingPrice = biddingPrice;
		this.auction = auction;
		this.bidder = bidder;
		this.tradingStatus = TradingStatus.WAITING;
	}

	//테스트용
	private Bidding(Long id, int biddingPrice, Auction auction, User bidder, TradingStatus tradingStatus) {
		this.id = id;
		this.biddingPrice = biddingPrice;
		this.auction = auction;
		this.bidder = bidder;
		this.tradingStatus = tradingStatus;
	}

	private Bidding(int biddingPrice, Auction auction, User bidder, TradingStatus tradingStatus) {
		this.biddingPrice = biddingPrice;
		this.auction = auction;
		this.bidder = bidder;
		this.tradingStatus = tradingStatus;
	}

	public static Bidding of(int biddingPrice, Auction auction, User bidder) {
		return Bidding.builder()
			.biddingPrice(biddingPrice)
			.auction(auction)
			.bidder(bidder)
			.build();
	}

	public static Bidding of(Long id, int biddingPrice, Auction auction, User bidder, TradingStatus status) {
		return new Bidding(id, biddingPrice, auction, bidder, status);
	}

	public static Bidding of(int biddingPrice, Auction auction, User bidder, TradingStatus status) {
		return new Bidding(biddingPrice, auction, bidder, status);
	}

	// 비즈니스 메서드
	public void updateTradingStatusComplete() {
		if (tradingStatus != TradingStatus.PROGRESSING) {
			throw new ValidationException(BiddingErrorCode.CAN_NOT_COMPLETE_TRADING);
		}
		tradingStatus = TradingStatus.COMPLETED;
	}

	public void updateTradingStatusCanceled() {
		if (tradingStatus != TradingStatus.PROGRESSING) {
			throw new ValidationException(BiddingErrorCode.CAN_NOT_CANCEL_TRADING);
		}
		tradingStatus = TradingStatus.CANCELED;
	}

	public void updateTradingStatusPreparing() {
		if (tradingStatus != TradingStatus.WAITING) {
			throw new ValidationException(BiddingErrorCode.CAN_NOT_PREPARE_TRADING);
		}
		tradingStatus = TradingStatus.PREPARING;
	}

	public void updateTradingStatusProgressing() {
		if (tradingStatus == TradingStatus.COMPLETED) {
			throw new ValidationException(BiddingErrorCode.CAN_NOT_PROGRESS_TRADING);
		}
		tradingStatus = TradingStatus.PROGRESSING;
	}
}
