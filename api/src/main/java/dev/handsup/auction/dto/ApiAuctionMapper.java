package dev.handsup.auction.dtos;

import static lombok.AccessLevel.*;

import dev.handsup.auction.dto.RegisterAuctionRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ApiAuctionMapper {
	public static RegisterAuctionRequest toRegisterAuctionRequest(RegisterAuctionApiRequest request){
		return new RegisterAuctionRequest(
			request.title(),
			request.productCategory(),
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
