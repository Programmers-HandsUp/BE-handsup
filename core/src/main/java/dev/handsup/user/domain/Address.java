package dev.handsup.user.domain;

import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Address {
	@Column(name = "tradeSi", nullable = false)
	private String si;

	@Column(name = "tradeGu", nullable = false)
	private String gu;

	@Column(name = "tradeDong", nullable = false)
	private String dong;

	@Builder
	private Address(String si, String gu, String dong) {
		this.si = si;
		this.gu = gu;
		this.dong = dong;
	}

	public static Address of(String si, String gu, String dong) {
		return Address.builder()
			.si(si)
			.gu(gu)
			.dong(dong)
			.build();
	}
}
