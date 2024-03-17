package dev.handsup.bidding.repository;

import static dev.handsup.bidding.domain.QBidding.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	@Transactional
	public void updateBiddingTradingStatus() {
		//하루 지난 각 경매들에 대한 최신 입찰 조회
		List<Long> latestBiddingIdsPerAuctions = queryFactory
			.select(bidding.id.max())
			.from(bidding)
			.where(bidding.auction.endDate.eq(LocalDate.now().minusDays(1)))
			.groupBy(bidding.auction)
			.fetch();

		// 해당 최신 입찰 상태를 준비중으로 업데이트
		queryFactory
			.update(bidding)
			.set(bidding.tradingStatus, TradingStatus.PREPARING)
			.where(bidding.id.in(latestBiddingIdsPerAuctions))
			.execute();
	}
}
