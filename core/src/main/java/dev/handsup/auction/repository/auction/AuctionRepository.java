package dev.handsup.auction.repository.auction;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.auction.domain.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
