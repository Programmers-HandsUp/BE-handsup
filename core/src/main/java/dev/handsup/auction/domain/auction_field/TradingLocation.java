package dev.handsup.auction.domain.auction_field;

import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TradingLocation {
	private static final String TRADING_LOCATION = "TradingLocation";

	@Column(name = "tradeSi")
	private String si;

	@Column(name = "tradeGu")
	private String gu;

	@Column(name = "tradeDong")
	private String dong;

	@Builder(access = PRIVATE)
	public TradingLocation(String si, String gu, String dong) {
		this.si = si;
		this.gu = gu;
		this.dong = dong;
	}

	public static TradingLocation of(String si, String gu, String dong) {
		return TradingLocation.builder()
			.si(si)
			.gu(gu)
			.dong(dong)
			.build();
	}
}
