package dev.handsup.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.annotation.NoAuth;
import dev.handsup.user.dto.UserApiMapper;
import dev.handsup.user.dto.JoinUserApiRequest;
import dev.handsup.user.dto.request.JoinUserRequest;
import dev.handsup.user.dto.response.JoinUserResponse;
import dev.handsup.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User API")
@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;

	@NoAuth
	@PostMapping("/api/users")
	@Operation(summary = "회원가입 API", description = "회원가입을 한다")
	@ApiResponse(useReturnTypeSchema = true)
	public ResponseEntity<JoinUserResponse> join(
		final @Valid @RequestBody JoinUserApiRequest request
	) {
		JoinUserRequest joinUserRequest = UserApiMapper.toJoinUserRequest(request);
		Long userId = userService.join(joinUserRequest);
		return ResponseEntity.ok(new JoinUserResponse(userId));
	}
}
