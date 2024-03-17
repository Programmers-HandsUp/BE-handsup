package dev.handsup.bidding.repository;

import java.util.Optional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.domain.Bidding;

public interface BiddingQueryRepository {

	Optional<Bidding> findWaitingBiddingLatest(Auction auction);
}
