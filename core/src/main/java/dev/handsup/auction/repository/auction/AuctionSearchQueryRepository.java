package dev.handsup.auction.repository.auction;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import dev.handsup.auction.domain.AuctionSearch;
import dev.handsup.auction.dto.request.AuctionSearchCondition;

public interface AuctionSearchQueryRepository {
	Slice<AuctionSearch> searchAuctions(AuctionSearchCondition auctionSearchCondition, Pageable pageable);

	Slice<AuctionSearch> sortAuctionByCriteria(String si, String gu, String dong, Pageable pageable);
}
