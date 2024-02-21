package dev.handsup.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.auth.dto.AuthApiMapper;
import dev.handsup.auth.dto.AuthApiRequest;
import dev.handsup.auth.dto.TokenReIssueApiRequest;
import dev.handsup.auth.dto.request.AuthRequest;
import dev.handsup.auth.dto.response.AuthResponse;
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
	public ResponseEntity<AuthResponse> login(
		@Valid @RequestBody AuthApiRequest request
	) {
		AuthRequest authRequest = AuthApiMapper.toAuthRequest(request);
		AuthResponse authResponse = authService.login(authRequest);
		return ResponseEntity.ok(authResponse);
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
	@Operation(summary = "토큰 재발급 API", description = "토큰을 재발급한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<String> reIssueAccessToken(
		@RequestBody TokenReIssueApiRequest tokenReIssueApiRequest
	) {
		String accessToken = authService.createAccessTokenByRefreshToken(tokenReIssueApiRequest.refreshToken());
		return ResponseEntity.ok(accessToken);
	}
}
