package dev.handsup.user.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.user.domain.User;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.dto.response.EmailAvailabilityResponse;
import dev.handsup.user.dto.response.JoinUserResponse;
import dev.handsup.user.dto.response.UserBuyHistoryResponse;
import dev.handsup.user.dto.response.UserDetailResponse;
import dev.handsup.user.dto.response.UserProfileResponse;
import dev.handsup.user.dto.response.UserReviewLabelResponse;
import dev.handsup.user.dto.response.UserReviewResponse;
import dev.handsup.user.dto.response.UserSaleHistoryResponse;
import dev.handsup.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "사용자 API")
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

	@GetMapping("/api/users")
	@Operation(summary = "로그인 된 사용자 정보 조회 API",
		description = "토큰으로 로그인 되어있는 사용자의 정보를 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<UserDetailResponse> getAuthorizedUser(
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		UserDetailResponse response = UserDetailResponse.of(user);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@GetMapping("/api/users/check-email")
	@Operation(summary = "이메일 중복 체크 API", description = "이메일이 이미 사용중인지 체크한다")
	public ResponseEntity<EmailAvailabilityResponse> checkEmailAvailability(
		@RequestParam("email") String email
	) {
		EmailAvailabilityResponse response = userService.isEmailAvailable(email);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@GetMapping("/api/users/{userId}/reviews/labels")
	@Operation(summary = "사용자 리뷰 라벨 조회 API", description = "특정 사용자의 리뷰 라벨을 조회한다")
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
	@Operation(summary = "사용자 리뷰 조회 API", description = "특정 사용자의 리뷰를 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<PageResponse<UserReviewResponse>> getUserReviews(
		@PathVariable Long userId,
		Pageable pageable
	) {
		PageResponse<UserReviewResponse> response = userService.getUserReviews(userId, pageable);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@GetMapping("/api/users/{userId}/profiles")
	@Operation(summary = "사용자 프로필 조회 API", description = "특정 사용자의 프로필을 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<UserProfileResponse> getUserProfile(
		@PathVariable Long userId
	) {
		UserProfileResponse response = userService.getUserProfile(userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/api/users/buys")
	@Operation(summary = "사용자 구매 내역 조회 API",
		description = "사용자 구매 내역을 전체/입찰 중/거래 중/완료 별로 경매의 최신 등록 순으로 조회한다")
	public ResponseEntity<PageResponse<UserBuyHistoryResponse>> getUserBuyHistory(
		@Parameter(hidden = true) @JwtAuthorization User user,
		@RequestParam(value = "auctionStatus", required = false) AuctionStatus auctionStatus,
		Pageable pageable
	) {
		PageResponse<UserBuyHistoryResponse> response = userService
			.getUserBuyHistory(user, auctionStatus, pageable);
		return ResponseEntity.ok(response);
	}

	@NoAuth
	@GetMapping("/api/users/{userId}/sales")
	@Operation(summary = "사용자 판매 내역 조회 API",
		description = "사용자 판매 내역을 전체/입찰 중/거래 중/완료 별로 경매의 최신 등록 순으로 조회한다")
	public ResponseEntity<PageResponse<UserSaleHistoryResponse>> getUserSaleHistory(
		@PathVariable Long userId,
		@RequestParam(value = "auctionStatus", required = false) AuctionStatus auctionStatus,
		Pageable pageable
	) {
		PageResponse<UserSaleHistoryResponse> response = userService
			.getUserSaleHistory(userId, auctionStatus, pageable);
		return ResponseEntity.ok(response);
	}

}
