package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.TradingStatus;
import dev.handsup.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class BiddingFixture {
	public static Bidding bidding(Auction auction, User user) {
		return Bidding.of(
			1L,
			40000,
			auction,
			user,
			TradingStatus.WAITING
		);
	}

	public static Bidding bidding(Auction auction, User user, TradingStatus status) {
		return Bidding.of(
			1L,
			40000,
			auction,
			user,
			status
		);
	}

	public static Bidding bidding(User user, Auction auction, TradingStatus status) {
		return Bidding.of(
			40000,
			auction,
			user,
			status
		);
	}

}
