package dev.handsup.auction.dto.response;

import java.util.List;

public record AuctionDetailResponse(

	Long auctionId,
	UserInfo sellerInfo,
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
	String tradeSi,
	String tradeGu,
	String tradeDong,
	int bookmarkCount,
	String createdAt

) {
	public static AuctionDetailResponse of(
		Long auctionId,
		Long sellerId,
		String nickname,
		String profileImageUrl,
		String dong,
		int score,
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
		String tradeSi,
		String tradeGu,
		String tradeDong,
		int bookmarkCount,
		String createdAt
	) {
		return new AuctionDetailResponse(
			auctionId,
			UserInfo.of(sellerId, nickname, profileImageUrl, dong, score),
			title,
			auctionStatus,
			productCategory,
			initPrice,
			currentBiddingPrice,
			endDate,
			productStatus,
			purchaseTime,
			description,
			tradeMethod,
			imageUrls,
			tradeSi, tradeGu, tradeDong,
			bookmarkCount,
			createdAt
		);
	}
}