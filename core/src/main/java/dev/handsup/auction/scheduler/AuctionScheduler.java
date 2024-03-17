package dev.handsup.auction.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.handsup.auction.repository.auction.AuctionQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {
	private final AuctionQueryRepository auctionQueryRepository;

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void updateAuctionStatus() {
		auctionQueryRepository.updateAuctionStatusTrading();
	}
}
