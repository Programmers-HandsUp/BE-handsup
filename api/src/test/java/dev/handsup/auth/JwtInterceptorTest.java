package dev.handsup.auth;

import static dev.handsup.auth.exception.AuthErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.method.HandlerMethod;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.auth.jwt.JwtInterceptor;
import dev.handsup.auth.service.JwtProvider;
import dev.handsup.common.exception.ValidationException;
import jakarta.servlet.http.HttpServletResponse;

@DisplayName("[JwtInterceptor 통합 테스트]")
@ExtendWith(MockitoExtension.class)
class JwtInterceptorTest {

	private final MockHttpServletRequest request = new MockHttpServletRequest();
	@InjectMocks
	private JwtInterceptor jwtInterceptor;
	@Mock
	private JwtProvider jwtProvider;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HandlerMethod handlerMethod;

	@Test
	@DisplayName("[토큰이 없고 @NoAuth 있으면 -> 통과]")
	void shouldPassWithoutTokenWhenNoAuthAnnotationIsPresent() {
		// given, when
		when(handlerMethod.getMethodAnnotation(NoAuth.class))
			.thenReturn(mock(NoAuth.class));
		// then
		assertThat(jwtInterceptor.preHandle(request, response, handlerMethod))
			.isTrue();
	}

	@Test
	@DisplayName("[토큰이 유효하다면 -> 통과]")
	void shouldPassWithValidToken() {
		// given
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer validToken");
		// when
		doNothing().when(jwtProvider).validateToken("validToken");
		// then
		assertThat(jwtInterceptor.preHandle(request, response, handlerMethod))
			.isTrue();
	}

	@Test
	@DisplayName("[토큰이 없고 @NoAuth 없으면 -> 실패]")
	void shouldThrowExceptionWhenTokenIsMissing() {
		assertThat(jwtInterceptor.preHandle(request, response, handlerMethod)).isFalse();
	}

	@Test
	@DisplayName("[토큰이 유효하지 않고 @NoAuth 없으면 -> 실패]")
	void shouldThrowExceptionWithInvalidToken() {
		// given
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer invalidToken");
		doThrow(new ValidationException(TOKEN_EXPIRED))
			.when(jwtProvider).validateToken("invalidToken");

		// when, then
		assertThat(jwtInterceptor.preHandle(request, response, handlerMethod)).isFalse();
	}

}
