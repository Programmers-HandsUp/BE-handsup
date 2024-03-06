package dev.handsup.chat.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dev.handsup.auction.dto.response.ChatRoomExistenceResponse;
import dev.handsup.auth.jwt.JwtAuthorization;
import dev.handsup.chat.dto.response.ChatRoomDetailResponse;

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
@RequestMapping("/api/auctions/chat-rooms")
public class ChatRoomApiController {

	private final ChatRoomService chatRoomService;

	@Operation(summary = "채팅방 등록 API", description = "채팅방을 등록한다")
	@ApiResponse(useReturnTypeSchema = true)
	@PostMapping
	public ResponseEntity<RegisterChatRoomResponse> registerChatRoom(
		@RequestParam("auctionId") Long auctionId,
		@RequestParam("bidderId") Long bidderId,
		@Parameter(hidden = true) @JwtAuthorization User seller
	) {
		RegisterChatRoomResponse response = chatRoomService.registerChatRoom(auctionId, bidderId, seller);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "유저 채팅방 모두 조회 API", description = "유저 아이디로 채팅방 목록을 모두 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping
	public ResponseEntity<PageResponse<ChatRoomSimpleResponse>> getUserChatRooms(
		Pageable pageable,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		PageResponse<ChatRoomSimpleResponse> response = chatRoomService.getUserChatRooms(user, pageable);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "채팅방 아이디로 채팅방 상세 조회", description = "채팅방 목록에서 채팅방 아이디로 채팅방을 상세 조회한다")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("/{chatRoomId}")
	public ResponseEntity<ChatRoomDetailResponse> getChatRoomWithId(
		@PathVariable("chatRoomId") Long chatRoomId,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		ChatRoomDetailResponse response = chatRoomService.getChatRoomWithId(chatRoomId, user);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "입찰 아이디로 채팅방 상세 조회", description = "입찰 아이디로 경매 아이디, 입찰자를 얻어 채팅방을 상세 조회한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("biddings/{biddingId}")
	public ResponseEntity<ChatRoomDetailResponse> getChatRoomWithBiddingId(
		@PathVariable("biddingId") Long biddingId,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		ChatRoomDetailResponse response = chatRoomService.getChatRoomWithBiddingId(biddingId, user);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "입찰 아이디로 채팅방 존재 여부 조회", description = "입찰 아이디로 경매 아이디, 입찰자를 얻어 채팅방 존재 여부를 확인한다.")
	@ApiResponse(useReturnTypeSchema = true)
	@GetMapping("biddings/{biddingId}/existence")
	public ResponseEntity<ChatRoomExistenceResponse> getChatRoomExistence(
		@PathVariable("biddingId") Long biddingId,
		@Parameter(hidden = true) @JwtAuthorization User user
	) {
		ChatRoomExistenceResponse response = chatRoomService.getChatRoomExistence(biddingId, user);
		return ResponseEntity.ok(response);
	}
}
