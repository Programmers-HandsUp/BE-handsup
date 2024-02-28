package dev.handsup.auth.jwt;

import static dev.handsup.auth.dto.AuthMapper.*;
import static dev.handsup.auth.exception.AuthErrorCode.*;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import dev.handsup.auth.service.JwtProvider;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtProvider jwtProvider;
	private final UserService userService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(JwtAuthorization.class);
	}

	@Override
	public Object resolveArgument(
		@NonNull MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

		if (httpServletRequest != null) {
			String accessToken = toAccessToken(httpServletRequest);
			Long userId = jwtProvider.getClaim(accessToken);
			return userService.getUserById(userId);
		}

		throw new NotFoundException(NOT_FOUND_REQUEST);
	}

}
