package dev.handsup.auction.dto.mapper;

import static lombok.AccessLevel.*;

import org.springframework.data.domain.Slice;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionDetailResponse;
import dev.handsup.auction.dto.response.AuctionSimpleResponse;
import dev.handsup.common.dto.PageResponse;
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
			request.si(),
			request.gu(),
			request.dong()
		);
	}

	public static AuctionDetailResponse toAuctionDetailResponse(Auction auction) {
		return AuctionDetailResponse.of(
			auction.getId(),
			auction.getSeller().getId(),
			auction.getTitle(),
			auction.getProduct().getProductCategory().getCategoryValue(),
			auction.getInitPrice(),
			auction.getEndDate().toString(),
			auction.getProduct().getStatus().getLabel(),
			auction.getProduct().getPurchaseTime().getLabel(),
			auction.getProduct().getDescription(),
			auction.getTradeMethod().getLabel(),
			auction.getTradingLocation().getSi(),
			auction.getTradingLocation().getGu(),
			auction.getTradingLocation().getDong(),
			auction.getBookmarkCount()
		);
	}

	public static AuctionSimpleResponse toAuctionSimpleResponse(Auction auction) {
		return AuctionSimpleResponse.of(
			auction.getId(),
			auction.getTitle(),
			auction.getInitPrice(),
			auction.getBookmarkCount(),
			auction.getTradingLocation().getDong(),
			auction.getCreatedAt().toLocalDate().toString(),
			null
		);

	}

	public static <T> PageResponse<T> toPageResponse(Slice<T> page) {
		return PageResponse.of(page);
	}

}
