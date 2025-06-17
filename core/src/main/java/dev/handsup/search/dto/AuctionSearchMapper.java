package dev.handsup.search.dto;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.search.domain.AuctionSearch;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.domain.product.Product;
import dev.handsup.recommend.dto.RecommendAuctionResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class AuctionSearchMapper {
	public static AuctionSearch toAuctionSearch(Auction auction) {
		Product product = auction.getProduct();
		return AuctionSearch.builder()
			.auctionId(auction.getId())
			.productId(product.getId())
			.category(product.getProductCategory().getValue())
			.title(auction.getTitle())
			.isNewProduct(auction.getStatus()== AuctionStatus.BIDDING)
			.imgUrl(product.getImages().get(0).toString())
			.endDate(auction.getEndDate())
			.tradingLocation(auction.getTradingLocation())
			.tradeMethod(auction.getTradeMethod())
			.createdAt(auction.getCreatedAt())
			.build();
	}

	public static AuctionSearchResponse toAuctionSearchResponse(AuctionSearch auctionSearch) {
		return AuctionSearchResponse.of(
			auctionSearch.getId(),
			auctionSearch.getTitle(),
			auctionSearch.getCurrentBiddingPrice(),
			auctionSearch.getImgUrl(),
			auctionSearch.getBookmarkCount(),
			auctionSearch.getTradingLocation().getDong(),
			auctionSearch.getCreatedAt().toString(),
			auctionSearch.isProgress()
		);
	}

	public static RecommendAuctionResponse toRecommendAuctionResponse(AuctionSearch auctionSearch) {
		return RecommendAuctionResponse.of(
			auctionSearch.getId(),
			auctionSearch.getTitle(),
			auctionSearch.getTradingLocation().getDong(),
			auctionSearch.getCurrentBiddingPrice(),
			auctionSearch.getImgUrl(),
			auctionSearch.getBookmarkCount(),
			auctionSearch.getBiddingCount(),
			auctionSearch.getCreatedAt().toString(),
			auctionSearch.getEndDate().atStartOfDay().toString()
		);
	}
}


