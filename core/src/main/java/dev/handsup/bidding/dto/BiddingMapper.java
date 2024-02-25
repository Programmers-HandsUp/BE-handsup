package dev.handsup.bidding.dto;

import static lombok.AccessLevel.*;

import org.springframework.data.domain.Slice;

import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.dto.response.BiddingResponse;
import dev.handsup.common.dto.PageResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class BiddingMapper {

	public static BiddingResponse toRegisterBiddingResponse(Bidding bidding) {
		return BiddingResponse.of(
			bidding.getBiddingPrice(),
			bidding.getAuction().getId(),
			bidding.getBidder().getNickname()
		);
	}

	public static <T> PageResponse<T> toBiddingPageResponse(Slice<T> page) {
		return new PageResponse<>(
			page.getContent(),
			page.getNumberOfElements(),
			page.hasNext()
		);
	}

	public static BiddingResponse toBiddingResponse(Bidding bidding) {
		return BiddingResponse.of(
			bidding.getBiddingPrice(),
			bidding.getAuction().getId(),
			bidding.getBidder().getNickname()
		);
	}
}
