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

	public static Auction auction(ProductCategory productCategory) {
		return Auction.of(
			"거의 새상품 버즈 팔아요",
			productCategory,
			10000,
			LocalDate.parse("2022-10-18"),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			"거의 새상품이에요",
			TradeMethod.DELIVER,
			"서울시",
			"성북구",
			"동선동"
		);
	}

	public static Auction auction(ProductCategory productCategory, Integer initPrice) {
		return Auction.of(
			"거의 새상품 버즈 팔아요",
			productCategory,
			initPrice,
			LocalDate.parse("2022-10-18"),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			"거의 새상품이에요",
			TradeMethod.DELIVER,
			"서울시",
			"성북구",
			"동선동"
		);
	}

	public static Auction auction(ProductCategory productCategory, ProductStatus productStatus) {
		return Auction.of(
			"거의 새상품 버즈 팔아요",
			productCategory,
			10000,
			LocalDate.parse("2022-10-18"),
			productStatus,
			PurchaseTime.UNDER_ONE_MONTH,
			"거의 새상품이에요",
			TradeMethod.DELIVER,
			"서울시",
			"성북구",
			"동선동"
		);
	}

	public static Auction auction(ProductCategory productCategory, TradeMethod tradeMethod) {
		return Auction.of(
			"거의 새상품 버즈 팔아요",
			productCategory,
			10000,
			LocalDate.parse("2022-10-18"),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			"거의 새상품이에요",
			tradeMethod,
			"서울시",
			"성북구",
			"동선동"
		);
	}
}
