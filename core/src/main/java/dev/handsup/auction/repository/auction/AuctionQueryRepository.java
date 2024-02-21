package dev.handsup.auction.repository.auction;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.dto.request.AuctionSearchCondition;

public interface AuctionQueryRepository {
	Slice<Auction> findAuctions(String title, AuctionSearchCondition auctionSearchCondition, Pageable pageable);
}