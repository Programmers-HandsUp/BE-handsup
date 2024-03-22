package dev.handsup.comment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.comment.domain.Comment;
import dev.handsup.comment.dto.request.RegisterCommentRequest;
import dev.handsup.comment.dto.response.CommentResponse;
import dev.handsup.comment.mapper.CommentMapper;
import dev.handsup.comment.repository.CommentRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.service.FCMService;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final AuctionRepository auctionRepository;
	private final CommentRepository commentRepository;
	private final FCMService fcmService;

	@Transactional
	public CommentResponse registerAuctionComment(Long auctionId, RegisterCommentRequest request, User user) {

		Auction auction = getAuctionById(auctionId);
		auction.validateIfCommentAvailable();
		Comment comment = commentRepository.save(CommentMapper.toComment(request, auction, user));

		sendMessage(user, auction);

		return CommentMapper.toCommentResponse(comment);
	}

	private void sendMessage(User user, Auction auction) {
		fcmService.sendMessage(
			user.getEmail(),
			user.getNickname(),
			auction.getSeller().getEmail(),
			NotificationType.BOOKMARK,
			auction
		);
	}

	@Transactional(readOnly = true)
	public PageResponse<CommentResponse> getAuctionComments(Long auctionId, Pageable pageable) {
		Auction auction = getAuctionById(auctionId);

		Slice<CommentResponse> responsePage = commentRepository
			.findByAuctionOrderByCreatedAtDesc(auction, pageable)
			.map(CommentMapper::toCommentResponse);

		return CommonMapper.toPageResponse(responsePage);
	}

	public Auction getAuctionById(Long auctionId) {
		return auctionRepository.findById(auctionId)
			.orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_AUCTION));
	}
}
