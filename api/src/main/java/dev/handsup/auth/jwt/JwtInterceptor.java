package dev.handsup.auth.jwt;

import static dev.handsup.auth.dto.AuthMapper.*;
import static dev.handsup.auth.exception.AuthErrorCode.*;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.auth.exception.AuthException;
import dev.handsup.auth.service.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

	private final JwtProvider jwtProvider;

	private boolean isPresentAnnotation(Object handler) {
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		return handlerMethod.getMethodAnnotation(NoAuth.class) != null;
	}

	@Override
	public boolean preHandle(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull Object handler
	) {
		if (isPresentAnnotation(handler)) {
			return true;
		}

		String accessToken = toAccessToken(request);
		if (accessToken == null) {
			throw new AuthException(NOT_FOUND_ACCESS_TOKEN_IN_REQUEST);
		}

		jwtProvider.validateToken(accessToken);
		return true;
	}
}
