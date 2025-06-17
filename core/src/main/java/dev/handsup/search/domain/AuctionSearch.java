package dev.handsup.search.domain;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.auction_field.TradingLocation;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AuctionSearch{

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "auction_search_id")
	private Long id;

	@Column(name = "auction_id", nullable = false, unique = true)
	private Long auctionId;

	@Column(name = "product_id", nullable = false, unique = true)
	private Long productId;

	@Column(name = "category", nullable = false)
	private String category;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "img_url", nullable = false)
	private String imgUrl;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@Embedded
	private TradingLocation tradingLocation;

	@Column(name = "trade_method", nullable = false)
	@Enumerated(STRING)
	private TradeMethod tradeMethod;

	@Column(name = "is_new_product", nullable = false)
	private boolean isNewProduct;

	@Column(name = "is_progress", nullable = false)
	private boolean isProgress = true;

	@Column(name = "current_bidding_price", nullable = false)
	private int currentBiddingPrice;

	@Column(name = "bidding_count", nullable = false)
	private int biddingCount = 0;

	@Column(name = "bookmark_count", nullable = false)
	private int bookmarkCount = 0;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Builder
	public AuctionSearch(Long auctionId, Long productId, String category, String title, String imgUrl,
		LocalDate endDate, TradingLocation tradingLocation, TradeMethod tradeMethod,
		int currentBiddingPrice, boolean isNewProduct, LocalDateTime createdAt) {
		this.auctionId = auctionId;
		this.productId = productId;
		this.category = category;
		this.isNewProduct = isNewProduct;
		this.title = title;
		this.imgUrl = imgUrl;
		this.endDate = endDate;
		this.tradingLocation = tradingLocation;
		this.tradeMethod = tradeMethod;
		this.currentBiddingPrice = currentBiddingPrice;
		this.createdAt = createdAt;
	}
}

