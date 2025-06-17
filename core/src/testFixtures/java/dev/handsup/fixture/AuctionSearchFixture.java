package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.test.util.ReflectionTestUtils;

import dev.handsup.search.domain.AuctionSearch;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.auction_field.TradingLocation;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuctionSearchFixture {
	static final String TITLE = "거의 새상품 버즈 팔아요";
	static final LocalDate END_DATE = LocalDate.parse("2022-10-18");
	static final String DIGITAL_DEVICE = "디지털 기기";
	static final String SI = "서울시";
	static final String GU = "성북구";
	static final String DONG = "동선동";
	static final String IMAGE_URL = "image.jpg";

	public static AuctionSearch auctionSearch(Long auctionId, Long productId, int currentBiddingPrice) {
		AuctionSearch auctionSearch = AuctionSearch.builder()
			.auctionId(auctionId)
			.productId(productId)
			.category(DIGITAL_DEVICE)
			.isNewProduct(false)
			.title(TITLE)
			.imgUrl(IMAGE_URL)
			.endDate(END_DATE)
			.tradingLocation(TradingLocation.of(SI, GU, DONG))
			.tradeMethod(TradeMethod.DIRECT)
			.createdAt(LocalDateTime.now())
			.build();
		ReflectionTestUtils.setField(auctionSearch, "currentBiddingPrice", currentBiddingPrice);
		return auctionSearch;
	}

	public static AuctionSearch auctionSearch(Long auctionId, Long productId,TradingLocation tradingLocation, LocalDate endDate) {
		return  AuctionSearch.builder()
			.auctionId(auctionId)
			.productId(productId)
			.category(DIGITAL_DEVICE)
			.isNewProduct(true)
			.title(TITLE)
			.imgUrl(IMAGE_URL)
			.endDate(endDate)
			.tradingLocation(tradingLocation)
			.tradeMethod(TradeMethod.DIRECT)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static AuctionSearch auctionSearch(Long auctionId, String category, Long productId) {
		return AuctionSearch.builder()
			.auctionId(auctionId)
			.productId(productId)
			.category(category)
			.isNewProduct(false)
			.title(TITLE)
			.imgUrl(IMAGE_URL)
			.endDate(END_DATE)
			.tradingLocation(TradingLocation.of(SI, GU, DONG))
			.tradeMethod(TradeMethod.DIRECT)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static AuctionSearch auctionSearch(Long auctionId, Long productId, TradeMethod tradeMethod) {
		return AuctionSearch.builder()
			.auctionId(auctionId)
			.productId(productId)
			.category(DIGITAL_DEVICE)
			.isNewProduct(false)
			.title(TITLE)
			.imgUrl(IMAGE_URL)
			.endDate(END_DATE)
			.tradingLocation(TradingLocation.of(SI, GU, DONG))
			.tradeMethod(tradeMethod)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static AuctionSearch auctionSearch(Long auctionId, Long productId, String keyword) {
		return  AuctionSearch.builder()
			.auctionId(auctionId)
			.productId(productId)
			.category(DIGITAL_DEVICE)
			.isNewProduct(false)
			.title(keyword)
			.imgUrl(IMAGE_URL)
			.endDate(END_DATE)
			.tradingLocation(TradingLocation.of(SI, GU, DONG))
			.tradeMethod(TradeMethod.DIRECT)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static AuctionSearch auctionSearch(Long auctionId, Long productId) {
		return  AuctionSearch.builder()
			.auctionId(auctionId)
			.productId(productId)
			.category(DIGITAL_DEVICE)
			.isNewProduct(true)
			.title(TITLE)
			.imgUrl(IMAGE_URL)
			.endDate(END_DATE)
			.tradingLocation(TradingLocation.of(SI, GU, DONG))
			.tradeMethod(TradeMethod.DIRECT)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static AuctionSearch auctionSearch(Long auctionId, Long productId,TradingLocation tradingLocation) {
		return  AuctionSearch.builder()
			.auctionId(auctionId)
			.productId(productId)
			.category(DIGITAL_DEVICE)
			.isNewProduct(true)
			.title(TITLE)
			.imgUrl(IMAGE_URL)
			.endDate(END_DATE)
			.tradingLocation(tradingLocation)
			.tradeMethod(TradeMethod.DIRECT)
			.createdAt(LocalDateTime.now())
			.build();
	}


	public static AuctionSearch auctionSearch(Long auctionId, Long productId, boolean isNewProduct) {
		return  AuctionSearch.builder()
			.auctionId(auctionId)
			.productId(productId)
			.category(DIGITAL_DEVICE)
			.isNewProduct(isNewProduct)
			.title(TITLE)
			.imgUrl(IMAGE_URL)
			.endDate(END_DATE)
			.tradingLocation(TradingLocation.of(SI, GU, DONG))
			.tradeMethod(TradeMethod.DIRECT)
			.createdAt(LocalDateTime.now())
			.build();
	}
}
