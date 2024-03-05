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
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import dev.handsup.auth.jwt.JwtAuthorizationArgumentResolver;
import dev.handsup.auth.service.JwtProvider;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.fixture.UserFixture;
import dev.handsup.user.domain.User;
import dev.handsup.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@DisplayName("[JwtAuthorization ArgumentResolver 통합 테스트]")
@ExtendWith(MockitoExtension.class)
class JwtAuthorizationArgumentResolverTest {

	private final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
	@InjectMocks
	private JwtAuthorizationArgumentResolver resolver;
	@Mock
	private UserService userService;
	@Mock
	private JwtProvider jwtProvider;
	@Mock
	private MethodParameter parameter;
	@Mock
	private ModelAndViewContainer mavContainer;
	@Mock
	private NativeWebRequest webRequest;
	@Mock
	private WebDataBinderFactory binderFactory;

	@Test
	@DisplayName("[토큰이 유효하면 User 엔티티를 반환한다]")
	void shouldResolveArgumentWithValidToken() {
		// given
		User user = UserFixture.user(1L);
		mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer validToken");
		when(userService.getUserById(user.getId())).thenReturn(user);
		when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(mockHttpServletRequest);
		when(jwtProvider.getClaim("validToken")).thenReturn(user.getId());

		// when
		Object o = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

		// then
		assertThat(o).isEqualTo(user);
	}

	@Test
	@DisplayName("[요청이 없으면 예외를 던진다]")
	void shouldThrowNotFoundExceptionWhenRequestMissing() {
		// given
		when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(null);

		// when + then
		assertThatThrownBy(() -> resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(NOT_FOUND_REQUEST.getMessage());
	}

}
