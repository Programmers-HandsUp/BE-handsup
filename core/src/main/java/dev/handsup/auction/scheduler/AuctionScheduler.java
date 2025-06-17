package dev.handsup.auction.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.auction.AuctionSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {
	private final AuctionRepository auctionRepository;
	private final AuctionSearchRepository auctionSearchRepository;

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void updateAuctionStatus() {
		auctionRepository.updateAuctionStatusAfterEndDate();
	}

	@Scheduled(cron = "0 */3 * * * *")
	public void updateAuctionSearch(){
		auctionSearchRepository.updateAuctionSearch();
	}
}
