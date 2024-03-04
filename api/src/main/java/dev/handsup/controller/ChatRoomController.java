package dev.handsup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.chat.dto.request.RegisterChatRoomResponse;
import dev.handsup.chat.service.ChatRoomService;
import dev.handsup.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "채팅방 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatRoomController {
	private final ChatRoomService chatRoomService;

	@Operation(summary = "채팅방 등록 API", description = "채팅방을 등록한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping
	public ResponseEntity<RegisterChatRoomResponse> registerChatroom(
		@Parameter(hidden = true) @JwtAuthorization User seller,
		@Parameter Long buyerId
	) {
		RegisterChatRoomResponse response = chatRoomService.registerChatRoom(seller, buyerId);
		return ResponseEntity.ok(response);
	}


	@Operation(summary = 가"채팅방 조회 API", description = "채팅방을 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/{chatRoomId}")
	public ResponseEntity<RegisterChatRoomResponse> getChatroom(
		@Parameter(hidden = true) @JwtAuthorization User seller,
		@PathVariable("chatRoomId") Long chatRoomId
	) {
		RegisterChatRoomResponse response = chatRoomService.getChatRoom(seller, buyerId);
		return ResponseEntity.ok(response);
	}
}
