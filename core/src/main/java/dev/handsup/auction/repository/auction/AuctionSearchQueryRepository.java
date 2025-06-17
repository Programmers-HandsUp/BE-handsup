package dev.handsup.auction.repository.auction;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import dev.handsup.search.domain.AuctionSearch;
import dev.handsup.search.dto.AuctionSearchCondition;

public interface AuctionSearchQueryRepository {
	Slice<AuctionSearch> searchAuctions(AuctionSearchCondition auctionSearchCondition, Pageable pageable);

	Slice<AuctionSearch> sortAuctionByCriteria(String si, String gu, String dong, Pageable pageable);

	Slice<AuctionSearch> findByProductCategories(List<String> productCategories, Pageable pageable);
}