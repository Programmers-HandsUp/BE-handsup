package dev.handsup.auth.domain;

import static jakarta.persistence.GenerationType.*;

import dev.handsup.common.entity.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "blacklist_token")
public class BlacklistToken extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "blacklist_token_id")
	private Long id;

	@Column(name = "refresh_token", nullable = false, unique = true)
	private String refreshToken;

	@Builder
	private BlacklistToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public static BlacklistToken from(String refreshToken) {
		return BlacklistToken.builder()
			.refreshToken(refreshToken)
			.build();
	}
}
