package dev.handsup.fixture;

import static lombok.AccessLevel.*;

import dev.handsup.auction.domain.Auction;
import dev.handsup.bidding.domain.Bidding;
import dev.handsup.bidding.domain.BiddingStatus;
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
			BiddingStatus.WAITING
		);
	}

}
