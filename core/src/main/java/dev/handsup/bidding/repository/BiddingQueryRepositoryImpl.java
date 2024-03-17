package dev.handsup.bidding.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.QAuction;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.QBidding;
import dev.handsup.bidding.domain.TradingStatus;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BiddingQueryRepositoryImpl implements BiddingQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Bidding> findWaitingBiddingLatest(Auction auction) {
		Bidding bidding = queryFactory.select(QBidding.bidding)
			.from(QBidding.bidding)
			.where(
				QAuction.auction.eq(auction),
				QBidding.bidding.tradingStatus.eq(TradingStatus.WAITING)
			)
			.orderBy(QBidding.bidding.createdAt.desc())
			.fetchFirst();
		return Optional.ofNullable(bidding);
	}
}
