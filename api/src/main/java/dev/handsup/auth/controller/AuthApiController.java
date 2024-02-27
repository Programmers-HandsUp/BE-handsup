package dev.handsup.auth.controller;

import static org.springframework.http.HttpHeaders.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.auth.dto.AuthMapper;
import dev.handsup.auth.dto.request.LoginRequest;
import dev.handsup.auth.dto.request.TokenReIssueRequest;
import dev.handsup.auth.dto.response.LoginResponse;
import dev.handsup.auth.dto.response.TokenReIssueResponse;
import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.auth.service.AuthService;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth API")
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthApiController {

	private final AuthService authService;

	@NoAuth
	@PostMapping("/login")
	@Operation(summary = "로그인 API", description = "로그인을 한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<LoginResponse> login(
		@Valid @RequestBody LoginRequest request
	) {
		LoginResponse response = authService.login(request);

		HttpHeaders headers = new HttpHeaders();
		headers.add(SET_COOKIE, AuthMapper.createCookie(
			"accessToken",
			response.accessToken(),
			7 * 24 * 60 * 60
		));
		headers.add(SET_COOKIE, AuthMapper.createCookie(
			"refreshToken",
			response.refreshToken(),
			14 * 24 * 60 * 60
		));

		return ResponseEntity.ok().headers(headers).body(response);
	}

	@PostMapping("/logout")
	@Operation(summary = "로그아웃 API", description = "로그아웃을 한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<HttpStatus> logout(
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		authService.logout(user);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@NoAuth
	@PostMapping("/token")
	@Operation(summary = "토큰 재발급 API", description = "리프레쉬 토큰으로 요청하여 액세스 토큰을 재발급 받는다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<TokenReIssueResponse> reIssueAccessToken(
		@RequestBody TokenReIssueRequest request
	) {
		TokenReIssueResponse response = authService.createAccessTokenByRefreshToken(request);

		HttpHeaders headers = new HttpHeaders();
		headers.add(SET_COOKIE, AuthMapper.createCookie(
			"accessToken",
			response.accessToken(),
			7 * 24 * 60 * 60
		));

		return ResponseEntity.ok().headers(headers).body(response);
	}
}
