package dev.handsup.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auth.domain.Auth;
import dev.handsup.auth.domain.BlacklistToken;
import dev.handsup.auth.domain.EncryptHelper;
import dev.handsup.auth.dto.request.LoginRequest;
import dev.handsup.auth.dto.request.TokenReIssueRequest;
import dev.handsup.auth.dto.response.LoginResponse;
import dev.handsup.auth.dto.response.TokenReIssueResponse;
import dev.handsup.auth.exception.AuthErrorCode;
import dev.handsup.auth.exception.AuthException;
import dev.handsup.auth.repository.AuthRepository;
import dev.handsup.auth.repository.BlacklistTokenRepository;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.user.domain.User;
import dev.handsup.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserService userService;
	private final JwtProvider jwtProvider;
	private final AuthRepository authRepository;
	private final EncryptHelper encryptHelper;
	private final BlacklistTokenRepository blacklistTokenRepository;

	private Auth getAuthByRefreshToken(String refreshToken) {
		return authRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NotFoundException(AuthErrorCode.NOT_FOUND_REFRESH_TOKEN));
	}

	private LoginResponse saveAuth(Long userId) {
		String refreshToken = jwtProvider.createRefreshToken(userId);
		String accessToken = jwtProvider.createAccessToken(userId);
		Optional<Auth> auth = authRepository.findByUserId(userId);

		auth.ifPresentOrElse(
			existingAuth -> authRepository.updateRefreshToken(existingAuth.getId(), refreshToken),
			() -> {
				Auth newAuth = Auth.builder()
					.userId(userId)
					.refreshToken(refreshToken)
					.build();

				authRepository.save(newAuth);
			}
		);

		return LoginResponse.of(refreshToken, accessToken);
	}

	@Transactional
	public LoginResponse login(LoginRequest request) {
		Long userId = userService.getUserByEmail(request.email()).getId();
		LoginResponse response = saveAuth(userId);
		String plainPassword = request.password();
		String hashedPassword = userService.getUserById(userId).getPassword();

		if (encryptHelper.isMatch(plainPassword, hashedPassword)) {
			return response;
		}
		throw new NotFoundException(AuthErrorCode.FAILED_LOGIN_BY_ANYTHING);
	}

	@Transactional
	public void logout(User user) {
		authRepository.findByUserId(user.getId()).ifPresentOrElse(
			auth ->
				blacklistTokenRepository.save(
					BlacklistToken.builder()
						.refreshToken(auth.getRefreshToken())
						.build()),
			() -> {
				throw new NotFoundException(AuthErrorCode.NOT_FOUND_USER_ID);
			}
		);
	}

	public TokenReIssueResponse createAccessTokenByRefreshToken(TokenReIssueRequest request) {
		boolean isBlacklisted = blacklistTokenRepository.existsByRefreshToken(request.refreshToken());
		if (isBlacklisted) {
			throw new AuthException(AuthErrorCode.BLACKLISTED_TOKEN);
		}

		Auth auth = getAuthByRefreshToken(request.refreshToken());
		Long userId = userService.getUserById(auth.getUserId()).getId();
		String accessToken = jwtProvider.createAccessToken(userId);

		return TokenReIssueResponse.from(accessToken);
	}

}
