package com.core.auction.domain;

import static com.core.auction.domain.auction_field.AuctionStatus.*;
import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;

import com.core.common.entity.TimeBaseEntity;
import com.core.auction.domain.auction_category.ProductCategory;
import com.core.auction.domain.auction_field.AuctionStatus;
import com.core.auction.domain.auction_field.Coordinate;
import com.core.auction.domain.auction_field.ProductStatus;
import com.core.auction.domain.auction_field.PurchaseTime;
import com.core.auction.domain.auction_field.TradeMethod;
import com.core.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
public class Auction extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "auction_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User seller;

	@Column(name = "title")
	private String title;

	@Column(name = "init_price")
	private int initPrice;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "description")
	private String description;

	@Column(name = "product_status")
	@Enumerated(STRING)
	private ProductStatus productStatus;

	@Column(name = "purchase_time")
	@Enumerated(STRING)
	private PurchaseTime purchaseTime;

	@Embedded
	private Coordinate coordinate;

	@Column(name = "trade_method")
	@Enumerated(STRING)
	private TradeMethod tradeMethod;

	@Column(name = "auction_status")
	@Enumerated(STRING)
	private AuctionStatus status;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "product_category_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private ProductCategory category;

	@Column(name = "bidding_count")
	private int biddingCount;

	@Column(name = "bookmark_count")
	private int bookmarkCount;

	@Builder
	public Auction(User seller, String title, int initPrice, LocalDate endDate, String description,
		ProductStatus productStatus, PurchaseTime purchaseTime, Coordinate coordinate, TradeMethod tradeMethod,
		ProductCategory category) {
		this.seller = seller;
		this.title = title;
		this.initPrice = initPrice;
		this.endDate = endDate;
		this.description = description;
		this.productStatus = productStatus;
		this.purchaseTime = purchaseTime;
		this.coordinate = coordinate;
		this.tradeMethod = tradeMethod;
		this.category = category;
		biddingCount = 0;
		bookmarkCount = 0;
		status = PROGRESS;
	}
}
