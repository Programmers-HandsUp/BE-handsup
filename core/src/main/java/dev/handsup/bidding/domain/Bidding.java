package dev.handsup.bidding.domain;

import static jakarta.persistence.ConstraintMode.*;
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

	@Column(name = "bidding_status", nullable = false)
	private BiddingStatus status;

	@Builder
	private Bidding(int biddingPrice, Auction auction, User bidder) {
		this.biddingPrice = biddingPrice;
		this.auction = auction;
		this.bidder = bidder;
		this.status = BiddingStatus.WAITING;
	}

	//테스트용
	private Bidding(Long id, int biddingPrice, Auction auction, User bidder, BiddingStatus status) {
		this.id = id;
		this.biddingPrice = biddingPrice;
		this.auction = auction;
		this.bidder = bidder;
		this.status = status;
	}

	public static Bidding of(int biddingPrice, Auction auction, User bidder) {
		return Bidding.builder()
			.biddingPrice(biddingPrice)
			.auction(auction)
			.bidder(bidder)
			.build();
	}

	public static Bidding of(Long id, int biddingPrice, Auction auction, User bidder, BiddingStatus status) {
		return new Bidding(id, biddingPrice, auction, bidder, status);
	}

	// 비즈니스 메서드
	public void completeBidding() {
		if (status != BiddingStatus.CHATTING) {
			throw new ValidationException(BiddingErrorCode.CAN_NOT_COMPLETE_BIDDING);
		}
		status = BiddingStatus.COMPLETED;
	}

	public void cancelBidding() {
		if (status != BiddingStatus.CHATTING){
			throw new ValidationException(BiddingErrorCode.CAN_NOT_CANCEL_BIDDING);
		}
		status = BiddingStatus.CANCELED;
	}

	public void prepareBidding() {
		if (status != BiddingStatus.WAITING){
			throw new ValidationException(BiddingErrorCode.CAN_NOT_PREPARE_BIDDING);
		}
		status = BiddingStatus.PREPARING;
	}
}
