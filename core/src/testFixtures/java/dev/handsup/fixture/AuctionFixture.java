package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import java.time.LocalDate;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuctionFixture {

	static final String TITLE = "거의 새상품 버즈 팔아요";
	static final String DATE = "2022-10-18";
	static final String DESCRIPTION = "거의 새상품이에요";
	static final String SI = "서울시";
	static final String GU = "성북구";
	static final String DONG = "동선동";

	// 아이디 지정한 기본 auction
	public static Auction auction() {
		return Auction.of(
			1L,
			UserFixture.user(),
			TITLE,
			ProductCategory.of("DIGITAL_DEVICE"),
			10000,
			LocalDate.parse(DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DELIVER,
			SI,
			GU,
			DONG
		);
	}

	public static Auction auction(ProductCategory productCategory) {
		return Auction.of(
			UserFixture.user(),
			TITLE,
			productCategory,
			10000,
			LocalDate.parse(DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DELIVER,
			SI,
			GU,
			DONG
		);
	}

	public static Auction auction(ProductCategory productCategory, Integer initPrice) {
		return Auction.of(
			UserFixture.user(),
			TITLE,
			productCategory,
			initPrice,
			LocalDate.parse(DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DELIVER,
			SI,
			GU,
			DONG
		);
	}

	public static Auction auction(ProductCategory productCategory, ProductStatus productStatus) {
		return Auction.of(
			UserFixture.user(),
			TITLE,
			productCategory,
			10000,
			LocalDate.parse(DATE),
			productStatus,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DELIVER,
			SI,
			GU,
			DONG
		);
	}

	public static Auction auction(ProductCategory productCategory, TradeMethod tradeMethod) {
		return Auction.of(
			UserFixture.user(),
			TITLE,
			productCategory,
			10000,
			LocalDate.parse(DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			tradeMethod,
			SI,
			GU,
			DONG
		);
	}

	public static Auction auction(ProductCategory productCategory, String title) {
		return Auction.of(
			UserFixture.user(),
			title,
			productCategory,
			10000,
			LocalDate.parse(DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DIRECT,
			SI,
			GU,
			DONG
		);
	}

	public static Auction auction(ProductCategory productCategory, String title, int initPrice) {
		return Auction.of(
			UserFixture.user(),
			title,
			productCategory,
			initPrice,
			LocalDate.parse(DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DIRECT,
			SI,
			GU,
			DONG
		);
	}
}
