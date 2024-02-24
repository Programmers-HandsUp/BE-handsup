package dev.handsup.auction.dto.mapper;

import static lombok.AccessLevel.*;

import org.springframework.data.domain.Slice;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.auction_field.PurchaseTime;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionResponse;
import dev.handsup.common.dto.PageResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuctionMapper {

	public static Auction toAuction(RegisterAuctionRequest request, ProductCategory productCategory) {

		ProductStatus productStatus = ProductStatus.of(request.productStatus());
		PurchaseTime purchaseTime = PurchaseTime.of(request.purchaseTime());
		TradeMethod tradeMethod = TradeMethod.of(request.tradeMethod());

		return Auction.of(
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

	public static AuctionResponse toAuctionResponse(Auction auction) {
		return AuctionResponse.builder()
			.auctionId(auction.getId())
			.title(auction.getTitle())
			.productCategory(auction.getProduct().getProductCategory().getCategoryValue())
			.initPrice(auction.getInitPrice())
			.endDate(auction.getEndDate())
			.productStatus(auction.getProduct().getStatus().getLabel())
			.purchaseTime(auction.getProduct().getPurchaseTime().getLabel())
			.description(auction.getProduct().getDescription())
			.tradeMethod(auction.getTradeMethod().getLabel())
			.si(auction.getTradingLocation().getSi())
			.gu(auction.getTradingLocation().getGu())
			.dong(auction.getTradingLocation().getDong())
			.build();
	}

	public static <T> PageResponse<T> toPageResponse(Slice<T> page) {
		return PageResponse.of(page);
	}

}
