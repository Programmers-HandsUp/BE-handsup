package dev.handsup.bidding.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.handsup.bidding.repository.BiddingQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BiddingScheduler {

	private BiddingQueryRepository biddingQueryRepository;

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void updateTradingStatus() {
		biddingQueryRepository.updateBiddingTradingStatus();
	}
}
