package com.example.handsup.domain.auction;

import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;

import com.example.handsup.common.TimeBaseEntity;
import com.example.handsup.domain.auction.auction_category.ProductCategory;
import com.example.handsup.domain.auction.auction_field.AuctionStatus;
import com.example.handsup.domain.auction.auction_field.Coordinate;
import com.example.handsup.domain.auction.auction_field.ProductStatus;
import com.example.handsup.domain.auction.auction_field.PurchaseTime;
import com.example.handsup.domain.auction.auction_field.TradeMethod;
import com.example.handsup.domain.user.User;

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
	@JoinColumn(name = "auction_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User user;

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

	@Column(name = "bookmark_count")
	private int bookmarkCount;

	@Builder
	public Auction(User user, String title, int initPrice, LocalDate endDate, String description,
		ProductStatus productStatus, PurchaseTime purchaseTime, Coordinate coordinate, TradeMethod tradeMethod,
		AuctionStatus status, ProductCategory category, int bookmarkCount) {
		this.user = user;
		this.title = title;
		this.initPrice = initPrice;
		this.endDate = endDate;
		this.description = description;
		this.productStatus = productStatus;
		this.purchaseTime = purchaseTime;
		this.coordinate = coordinate;
		this.tradeMethod = tradeMethod;
		this.status = status;
		this.category = category;
		this.bookmarkCount = bookmarkCount;
	}
}