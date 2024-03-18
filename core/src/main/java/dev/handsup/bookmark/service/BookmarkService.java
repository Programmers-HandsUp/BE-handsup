package dev.handsup.bookmark.service;

import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.bookmark.domain.Bookmark;
import dev.handsup.bookmark.dto.BookmarkMapper;
import dev.handsup.bookmark.dto.EditBookmarkResponse;
import dev.handsup.bookmark.dto.FindUserBookmarkResponse;
import dev.handsup.bookmark.dto.GetBookmarkStatusResponse;
import dev.handsup.bookmark.exception.BookmarkErrorCode;
import dev.handsup.bookmark.repository.BookmarkRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.notification.domain.NotificationType;
import dev.handsup.notification.service.FCMService;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final AuctionRepository auctionRepository;
	private final FCMService fcmService;

	@Transactional
	public EditBookmarkResponse addBookmark(User user, Long auctionId) {
		Auction auction = getAuctionById(auctionId);
		validateIfBookmarkExists(user, auction);
		validateSelfBookmark(user, auction);
		auction.increaseBookmarkCount();
		Bookmark bookmark = BookmarkMapper.toBookmark(user, auction);

		bookmarkRepository.save(bookmark);

		// user 는 sender
		fcmService.sendMessage(
			user.getEmail(),
			user.getNickname(),
			auction.getSeller().getEmail(),
			NotificationType.BOOKMARK,
			auction
		);

		return BookmarkMapper.toEditBookmarkResponse(auction.getBookmarkCount());
	}

	private void validateSelfBookmark(User user, Auction auction) {
		if (Objects.equals(auction.getSeller().getId(), user.getId())) {
			throw new ValidationException(BookmarkErrorCode.NOT_ALLOW_SELF_BOOKMARK);
		}
	}

	@Transactional
	public EditBookmarkResponse cancelBookmark(User user, Long auctionId) {
		Auction auction = getAuctionById(auctionId);
		deleteBookmark(getBookmarkByUserAndAuction(user, auction));
		auction.decreaseBookmarkCount();

		return BookmarkMapper.toEditBookmarkResponse(auction.getBookmarkCount());
	}

	@Transactional(readOnly = true)
	public GetBookmarkStatusResponse getBookmarkStatus(User user, Long auctionId) {
		Auction auction = getAuctionById(auctionId);
		boolean isBookmarked = bookmarkRepository.existsByUserAndAuction(user, auction);

		return BookmarkMapper.toGetBookmarkStatusResponse(isBookmarked);
	}

	@Transactional(readOnly = true)
	public PageResponse<FindUserBookmarkResponse> findUserBookmarks(User user, Pageable pageable) {
		Slice<FindUserBookmarkResponse> auctionResponsePage
			= auctionRepository.findBookmarkAuction(user, pageable)
			.map(BookmarkMapper::toFindUserBookmarkResponse);

		return CommonMapper.toPageResponse(auctionResponsePage);
	}

	private Auction getAuctionById(Long auctionId) {
		return auctionRepository.findById(auctionId).
			orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_AUCTION));
	}

	private void validateIfBookmarkExists(User user, Auction auction) {
		bookmarkRepository.findByUserAndAuction(user, auction).ifPresent(bookmark -> {
			throw new ValidationException(BookmarkErrorCode.ALREADY_EXISTS_BOOKMARK);
		});
	}

	private Bookmark getBookmarkByUserAndAuction(User user, Auction auction) {
		return bookmarkRepository.findByUserAndAuction(user, auction)
			.orElseThrow(() -> new ValidationException(BookmarkErrorCode.NOT_FOUND_BOOKMARK));
	}

	private void deleteBookmark(Bookmark bookmark) {
		bookmarkRepository.deleteById(bookmark.getId());
	}

}
