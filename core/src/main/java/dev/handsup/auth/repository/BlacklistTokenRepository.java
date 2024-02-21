package dev.handsup.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.handsup.auth.domain.BlacklistToken;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {
	boolean existsByRefreshToken(String refreshToken);
}
