package dev.handsup.auction.dto.response;

import java.util.List;

public record AuctionDetailResponse(

	Long auctionId,
	Long sellerId,
	String title,
	String productCategory,
	String auctionStatus,
	int initPrice,
	int currentBiddingPrice,
	String endDate,
	String productStatus,
	String purchaseTime,
	String description,
	String tradeMethod,
	List<String> imageUrls,
	String si,
	String gu,
	String dong,
	int bookmarkCount,
	String createdAt

) {
	public static AuctionDetailResponse of(
		Long auctionId,
		Long sellerId,
		String title,
		String auctionStatus,
		String productCategory,
		int initPrice,
		int currentBiddingPrice,
		String endDate,
		String productStatus,
		String purchaseTime,
		String description,
		String tradeMethod,
		List<String> imageUrls,
		String si,
		String gu,
		String dong,
		int bookmarkCount,
		String createdAt
	) {
		return new AuctionDetailResponse(
			auctionId, sellerId, title, auctionStatus, productCategory, initPrice, currentBiddingPrice, endDate,
			productStatus,
			purchaseTime, description,
			tradeMethod, imageUrls, si, gu, dong, bookmarkCount, createdAt);
	}
}