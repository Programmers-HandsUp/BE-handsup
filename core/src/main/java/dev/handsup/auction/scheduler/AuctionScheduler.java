package dev.handsup.auction.scheduler;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.repository.auction.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {
	private final AuctionRepository auctionRepository;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void updateAuctionStatus() {
		auctionRepository.updateAuctionStatus(AuctionStatus.BIDDING, AuctionStatus.TRADING, LocalDate.now());
	}
}
