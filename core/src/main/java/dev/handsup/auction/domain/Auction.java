package dev.handsup.auction.domain;

import static dev.handsup.auction.domain.auction_field.AuctionStatus.*;
import static dev.handsup.common.exception.CommonValidationError.*;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.ConstraintMode.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.util.Assert;

import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.auction_field.TradingLocation;
import dev.handsup.auction.domain.product.Product;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.common.entity.TimeBaseEntity;
import dev.handsup.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Auction extends TimeBaseEntity {

	private static final String AUCTION_STRING = "auction";

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "auction_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "seller_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User seller;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "buyer_id", foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User buyer;

	@Column(name = "title", nullable = false)
	private String title;

	@OneToOne(fetch = LAZY, cascade = ALL)
	@JoinColumn(
		name = "product_id",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private Product product;

	@Column(name = "init_price", nullable = false)
	private int initPrice;

	@Column(name = "current_bidding_price", nullable = false)
	private int currentBiddingPrice;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Embedded
	private TradingLocation tradingLocation;

	@Column(name = "trade_method", nullable = false)
	@Enumerated(STRING)
	private TradeMethod tradeMethod;

	@Column(name = "auction_status", nullable = false)
	@Enumerated(STRING)
	private AuctionStatus status = PROGRESS;

	@Column(name = "bidding_count", nullable = false)
	private int biddingCount = 0;

	@Column(name = "bookmark_count", nullable = false)
	private int bookmarkCount = 0;

	@Builder
	private Auction(User seller, String title, Product product, int initPrice, LocalDate endDate,
		TradingLocation tradingLocation,
		TradeMethod tradeMethod) {
		Assert.hasText(title, getNotEmptyMessage(AUCTION_STRING, "title"));
		this.seller = seller;
		this.title = title;
		this.product = product;
		this.initPrice = initPrice;
		this.currentBiddingPrice = initPrice;
		this.endDate = endDate;
		this.tradingLocation = tradingLocation;
		this.tradeMethod = tradeMethod;
	}

	// 테스트용 생성자
	private Auction(Long id, User seller, String title, Product product, int initPrice, LocalDate endDate,
		TradingLocation tradingLocation, TradeMethod tradeMethod) {
		Assert.hasText(title, getNotEmptyMessage(AUCTION_STRING, "title"));
		this.id = id;
		this.seller = seller;
		this.title = title;
		this.product = product;
		this.initPrice = initPrice;
		this.currentBiddingPrice = initPrice;
		this.endDate = endDate;
		this.tradingLocation = tradingLocation;
		this.tradeMethod = tradeMethod;
	}

	public static Auction of(User seller, String title, ProductCategory productCategory, int initPrice,
		LocalDate endDate,
		ProductStatus status, PurchaseTime purchaseTime, String description, TradeMethod tradeMethod,
		List<String> imageUrls, String si, String gu, String dong) {
		return Auction.builder()
			.seller(seller)
			.title(title)
			.product(Product.of(status, description, purchaseTime, productCategory, imageUrls))
			.initPrice(initPrice)
			.endDate(endDate)
			.tradingLocation(TradingLocation.of(si, gu, dong))
			.tradeMethod(tradeMethod)
			.build();
	}

	public static Auction of(Long id, User seller, String title, ProductCategory productCategory, int initPrice,
		LocalDate endDate, ProductStatus status, PurchaseTime purchaseTime, String description,
		TradeMethod tradeMethod, List<String> imageUrls, String si, String gu, String dong) {
		return new Auction(
			id,
			seller,
			title,
			Product.of(status, description, purchaseTime, productCategory, imageUrls),
			initPrice,
			endDate,
			TradingLocation.of(si, gu, dong),
			tradeMethod
		);
	}

	public void changeAuctionStatusTrading() {
		status = AuctionStatus.TRADING;
	}

	public void increaseBookmarkCount() {
		bookmarkCount++;
	}

	public void decreaseBookmarkCount() {
		bookmarkCount--;
	}

	public void updateCurrentBiddingPrice(int currentBiddingPrice) {
		this.currentBiddingPrice = currentBiddingPrice;
	}
}
