package dev.handsup.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.chat.dto.response.ChatRoomSimpleResponse;
import dev.handsup.chat.dto.response.RegisterChatRoomResponse;
import dev.handsup.chat.service.ChatRoomService;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "채팅방 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions/chatrooms")
public class ChatRoomApiController {
	private final ChatRoomService chatRoomService;

	@Operation(summary = "채팅방 등록 API", description = "채팅방을 등록한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping
	public ResponseEntity<RegisterChatRoomResponse> registerChatRoom(
		@RequestParam("auctionId") Long auctionId,
		@RequestParam("buyerId") Long buyerId,
		@Parameter(hidden = true) @JwtAuthorization User seller
	) {
		RegisterChatRoomResponse response = chatRoomService.registerChatRoom(auctionId, buyerId, seller);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "유저 채팅방 모두 조회 API", description = "유저 아이디로 채팅방 목록을 모두 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping
	public ResponseEntity<PageResponse<ChatRoomSimpleResponse>> getUserChatRooms(
		@Parameter(hidden = true) @JwtAuthorization User user,
		Pageable pageable
	) {
		PageResponse<ChatRoomSimpleResponse> response = chatRoomService.getUserChatRooms(user, pageable);
		return ResponseEntity.ok(response);
	}
}
