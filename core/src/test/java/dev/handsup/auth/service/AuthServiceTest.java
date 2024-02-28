package dev.handsup.auth.service;

import static dev.handsup.auth.exception.AuthErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.handsup.auth.domain.Auth;
import dev.handsup.auth.domain.BlacklistToken;
import dev.handsup.auth.domain.EncryptHelper;
import dev.handsup.auth.dto.request.LoginRequest;
import dev.handsup.auth.dto.response.LoginDetailResponse;
import dev.handsup.auth.exception.AuthErrorCode;
import dev.handsup.auth.repository.AuthRepository;
import dev.handsup.auth.repository.BlacklistTokenRepository;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.service.UserService;

@DisplayName("[AuthService 테스트]")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	private final User user = UserFixture.user(1L);
	private final LoginRequest loginRequest = LoginRequest.of(user.getEmail(), user.getPassword());
	@InjectMocks
	private AuthService authService;
	@Mock
	private AuthRepository authRepository;
	@Mock
	private UserService userService;
	@Mock
	private JwtProvider jwtProvider;
	@Mock
	private EncryptHelper encryptHelper;
	@Mock
	private BlacklistTokenRepository blacklistTokenRepository;

	@Test
	@DisplayName("[로그인 성공 시 토큰을 발급한다]")
	void loginSuccessTest() {
		// given
		when(userService.getUserByEmail(loginRequest.email())).thenReturn(user);
		when(userService.getUserById(1L)).thenReturn(user);
		when(encryptHelper.isMatch(anyString(), anyString())).thenReturn(true);
		Auth anyAuth = new Auth();
		when(authRepository.findByUserId(user.getId())).thenReturn(Optional.of(anyAuth));
		when(jwtProvider.createAccessToken(anyLong())).thenReturn("access-token");
		when(jwtProvider.createRefreshToken(anyLong())).thenReturn("refresh-token");

		// when
		LoginDetailResponse loginDetailResponse = authService.login(loginRequest);

		// then
		assertEquals("access-token", loginDetailResponse.accessToken());
		assertEquals("refresh-token", loginDetailResponse.refreshToken());
	}

	@Test
	@DisplayName("[로그인 실패 시 예외를 던진다]")
	void loginFailTest() {
		// given
		when(userService.getUserByEmail(loginRequest.email())).thenReturn(user);
		when(userService.getUserById(1L)).thenReturn(user);
		when(encryptHelper.isMatch(anyString(), anyString())).thenReturn(false);

		// when & then
		assertThatThrownBy(() -> authService.login(loginRequest))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(AuthErrorCode.FAILED_LOGIN_BY_ANYTHING.getMessage());
	}

	@Test
	@DisplayName("[로그아웃 성공 시 블랙리스트에 토큰을 추가한다]")
	void logoutSuccessTest() {
		// given
		Long userId = 1L;
		Auth auth = Auth.of(userId, "refresh-token");
		when(authRepository.findByUserId(userId)).thenReturn(Optional.of(auth));

		// when
		authService.logout(user);

		// then
		verify(blacklistTokenRepository).save(any(BlacklistToken.class));
	}

	@Test
	@DisplayName("[로그아웃 실패 시 NotFoundException을 던진다]")
	void logoutFailTest() {
		// given
		Long userId = 1L;
		when(authRepository.findByUserId(userId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> authService.logout(user))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(NOT_FOUND_USER_ID.getMessage());
	}
}
