package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.util.List;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuctionFixture {

	static final String TITLE = "거의 새상품 버즈 팔아요";
	static final String END_DATE = "2022-10-18";
	static final String DIGITAL_DEVICE = "디지털 기기";
	static final String DESCRIPTION = "거의 새상품이에요";
	static final String SI = "서울시";
	static final String GU = "성북구";
	static final String DONG = "동선동";
	static final String IMAGE_URL = "image.jpg";

	// 아이디 지정한 기본 auction
	public static Auction auction() {
		return Auction.of(
			3L,
			UserFixture.user(),
			TITLE,
			ProductCategory.from(DIGITAL_DEVICE),
			10000,
			LocalDate.parse(END_DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DELIVER,
			List.of(IMAGE_URL),
			SI,
			GU,
			DONG
		);
	}

	public static Auction auction(User seller) {
		return Auction.of(
			1L,
			seller,
			TITLE,
			ProductCategory.from(DIGITAL_DEVICE),
			10000,
			LocalDate.parse(END_DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DELIVER,
			List.of(IMAGE_URL),
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
			LocalDate.parse(END_DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DELIVER,
			List.of(IMAGE_URL),
			SI,
			GU,
			DONG
		);
	}

	public static Auction auction(User seller, ProductCategory productCategory) {
		return Auction.of(
			1L,
			seller,
			TITLE,
			productCategory,
			10000,
			LocalDate.parse(END_DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DELIVER,
			List.of(IMAGE_URL),
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
			LocalDate.parse(END_DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DELIVER,
			List.of(IMAGE_URL),
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
			LocalDate.parse(END_DATE),
			productStatus,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DELIVER,
			List.of(IMAGE_URL),
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
			LocalDate.parse(END_DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			tradeMethod,
			List.of(IMAGE_URL),
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
			LocalDate.parse(END_DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DIRECT,
			List.of(IMAGE_URL),
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
			LocalDate.parse(END_DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DIRECT,
			List.of(IMAGE_URL),
			SI,
			GU,
			DONG
		);
	}

	public static Auction auction(ProductCategory productCategory, String si, String gu, String dong) {
		return Auction.of(
			UserFixture.user(),
			TITLE,
			productCategory,
			10000,
			LocalDate.parse(END_DATE),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DIRECT,
			List.of(IMAGE_URL),
			si,
			gu,
			dong
		);
	}

	public static Auction auction(ProductCategory productCategory, String endDate, String si, String gu, String dong) {
		return Auction.of(
			UserFixture.user(),
			TITLE,
			productCategory,
			10000,
			LocalDate.parse(endDate),
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DIRECT,
			List.of(IMAGE_URL),
			si,
			gu,
			dong
		);
	}

	public static Auction auction(ProductCategory productCategory, LocalDate endDate) {
		return Auction.of(
			UserFixture.user(),
			TITLE,
			productCategory,
			10000,
			endDate,
			ProductStatus.NEW,
			PurchaseTime.UNDER_ONE_MONTH,
			DESCRIPTION,
			TradeMethod.DIRECT,
			List.of(IMAGE_URL),
			SI,
			GU,
			DONG
		);
	}

}
