package dev.handsup.auction.dto;

import static lombok.AccessLevel.*;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ApiAuctionMapper {
	public static RegisterAuctionRequest toRegisterAuctionRequest(RegisterAuctionApiRequest request){
		return new RegisterAuctionRequest(
			request.title(),
			request.category(),
			request.initPrice(),
			request.endDate(),
			request.productStatus(),
			request.purchaseTime(),
			request.description(),
			request.tradeMethod(),
			request.si(),
			request.gu(),
			request.dong()
		);
	}
}
