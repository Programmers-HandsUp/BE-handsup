package dev.handsup.auction.dto.response;

public record AuctionDetailResponse(

	Long auctionId,
	Long sellerId,
	String title,
	String productCategory,
	int initPrice,
	String endDate,
	String productStatus,
	String purchaseTime,
	String description,
	String tradeMethod,
	String si,
	String gu,
	String dong,

	int bookmarkCount
) {
	public static AuctionDetailResponse of(
		Long auctionId,
		Long sellerId,
		String title,
		String productCategory,
		int initPrice,
		String endDate,
		String productStatus,
		String purchaseTime,
		String description,
		String tradeMethod,
		String si,
		String gu,
		String dong,
		int bookmarkCount
	) {
		return new AuctionDetailResponse(
			auctionId, sellerId, title, productCategory, initPrice, endDate, productStatus, purchaseTime, description,
			tradeMethod, si, gu, dong, bookmarkCount);
	}
}
