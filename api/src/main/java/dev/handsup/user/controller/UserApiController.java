package dev.handsup.user.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.user.dto.request.EmailAvailibilityRequest;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.dto.response.EmailAvailabilityResponse;
import dev.handsup.user.dto.response.JoinUserResponse;
import dev.handsup.user.dto.response.UserReviewLabelResponse;
import dev.handsup.user.dto.response.UserReviewResponse;
import dev.handsup.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원 API")
@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;

	@NoAuth
	@PostMapping("/api/users")
	@Operation(summary = "회원가입 API", description = "회원가입을 한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<JoinUserResponse> join(
		final @Valid @RequestBody JoinUserRequest request
	) {
		Long userId = userService.join(request);
		JoinUserResponse response = JoinUserResponse.from(userId);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@GetMapping("/api/users/check-email")
	@Operation(summary = "이메일 중복 체크 API", description = "이메일이 이미 사용중인지 체크한다")
	public ResponseEntity<EmailAvailabilityResponse> checkEmailAvailability(
		@Valid @RequestBody EmailAvailibilityRequest request
	) {
		EmailAvailabilityResponse response = userService.isEmailAvailable(request);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@GetMapping("/api/users/{userId}/reviews/labels")
	@Operation(summary = "유저 리뷰 라벨 조회 API", description = "특정 유저의 리뷰 라벨을 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<PageResponse<UserReviewLabelResponse>> getUserReviewLabels(
		@PathVariable Long userId,
		Pageable pageable
	) {
		PageResponse<UserReviewLabelResponse> response = userService.getUserReviewLabels(userId, pageable);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@GetMapping("/api/users/{userId}/reviews")
	@Operation(summary = "유저 리뷰 조회 API", description = "특정 유저의 리뷰를 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<PageResponse<UserReviewResponse>> getUserReviews(
		@PathVariable Long userId,
		Pageable pageable
	) {
		PageResponse<UserReviewResponse> response = userService.getUserReviews(userId, pageable);
		return ResponseEntity.ok(response);
	}
}
