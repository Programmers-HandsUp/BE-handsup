package dev.handsup.auction.dto.mapper;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.auction_field.TradingLocation;
import dev.handsup.auction.domain.product.Product;
import dev.handsup.auction.domain.product.ProductImage;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionDetailResponse;
import dev.handsup.auction.dto.response.AuctionSimpleResponse;
import dev.handsup.auction.dto.response.RecommendAuctionResponse;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuctionMapper {
	public static Auction toAuction(RegisterAuctionRequest request, ProductCategory productCategory, User user) {

		ProductStatus productStatus = ProductStatus.of(request.productStatus());
		PurchaseTime purchaseTime = PurchaseTime.of(request.purchaseTime());
		TradeMethod tradeMethod = TradeMethod.of(request.tradeMethod());

		return Auction.of(
			user,
			request.title(),
			productCategory,
			request.initPrice(),
			request.endDate(),
			productStatus,
			purchaseTime,
			request.description(),
			tradeMethod,
			request.imageUrls(),
			request.si(),
			request.gu(),
			request.dong()
		);
	}

	public static AuctionDetailResponse toAuctionDetailResponse(Auction auction) {
		User seller = auction.getSeller();
		Product product = auction.getProduct();
		TradingLocation tradingLocation = auction.getTradingLocation();
		String si = null;
		String gu = null;
		String dong = null;

		if (tradingLocation != null) {
			si = tradingLocation.getSi();
			gu = tradingLocation.getGu();
			dong = tradingLocation.getDong();
		}

		return AuctionDetailResponse.of(
			auction.getId(),
			seller.getId(),
			seller.getNickname(),
			seller.getProfileImageUrl(),
			seller.getAddress().getDong(),
			seller.getScore(),
			auction.getTitle(),
			product.getProductCategory().getValue(),
			auction.getStatus().getLabel(),
			auction.getInitPrice(),
			auction.getCurrentBiddingPrice(),
			auction.getEndDate().toString(),
			product.getStatus().getLabel(),
			product.getPurchaseTime().getLabel(),
			product.getDescription(),
			auction.getTradeMethod().getLabel(),
			product.getImages().stream()
				.map(ProductImage::getImageUrl).toList(),
			si, gu, dong,
			auction.getBookmarkCount(),
			auction.getCreatedAt().toString()
		);
	}

	public static AuctionSimpleResponse toAuctionSimpleResponse(Auction auction) {
		return AuctionSimpleResponse.of(
			auction.getId(),
			auction.getTitle(),
			auction.getCurrentBiddingPrice(),
			auction.getProduct().getImages().get(0).getImageUrl(),
			auction.getBookmarkCount(),
			auction.getTradingLocation().getDong(),
			auction.getCreatedAt().toString()
		);
	}

	public static RecommendAuctionResponse toRecommendAuctionResponse(Auction auction) {
		return RecommendAuctionResponse.of(
			auction.getId(),
			auction.getTitle(),
			auction.getTradingLocation().getDong(),
			auction.getCurrentBiddingPrice(),
			auction.getProduct().getImages().get(0).getImageUrl(),
			auction.getBookmarkCount(),
			auction.getBiddingCount(),
			auction.getCreatedAt().toString(),
			auction.getEndDate().toString()
		);
	}

}
