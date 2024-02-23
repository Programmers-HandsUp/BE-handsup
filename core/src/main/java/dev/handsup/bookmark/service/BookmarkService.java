package dev.handsup.bookmark.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.bookmark.domain.Bookmark;
import dev.handsup.bookmark.dto.BookmarkMapper;
import dev.handsup.bookmark.dto.CheckBookmarkStatusResponse;
import dev.handsup.bookmark.dto.EditBookmarkResponse;
import dev.handsup.bookmark.dto.FindUserBookmarkResponse;
import dev.handsup.bookmark.exception.BookmarkErrorCode;
import dev.handsup.bookmark.repository.BookmarkRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {
	private final BookmarkRepository bookmarkRepository;
	private final AuctionRepository auctionRepository;

	@Transactional
	public EditBookmarkResponse addBookmark(User user, Long auctionId){
		Auction auction = getAuctionEntity(auctionId);
		validateIfBookmarkExists(user, auction);
		Bookmark bookmark = BookmarkMapper.toBookmark(user, auction);
		auction.increaseBookmarkCount();
		bookmarkRepository.save(bookmark);

		return BookmarkMapper.toEditBookmarkResponse(auction.getBookmarkCount());
	}
	@Transactional
	public EditBookmarkResponse cancelBookmark(User user, Long auctionId){
		Auction auction = getAuctionEntity(auctionId);
		deleteBookmark(getBookmarkEntity(user, auction));
		auction.decreaseBookmarkCount();

		return BookmarkMapper.toEditBookmarkResponse(auction.getBookmarkCount());
	}

	@Transactional(readOnly = true)
	public CheckBookmarkStatusResponse checkBookmarkStatus(User user, Long auctionId) {
		Auction auction = getAuctionEntity(auctionId);
		boolean isBookmarked= bookmarkRepository.existsByUserAndAuction(user, auction);

		return BookmarkMapper.toCheckBookmarkResponse(isBookmarked);
	}

	@Transactional(readOnly = true)
	public PageResponse<FindUserBookmarkResponse> findUserBookmarks(User user, Pageable pageable){
		Slice<FindUserBookmarkResponse> auctionResponsePage
			= auctionRepository.findBookmarkAuction(user, pageable)
			.map(BookmarkMapper::toFindUserBookmarkResponse);

		return BookmarkMapper.toPageResponse(auctionResponsePage);
	}

	private Auction getAuctionEntity(Long auctionId) {
		return auctionRepository.findById(auctionId).
			orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY));
	}

	private void validateIfBookmarkExists(User user, Auction auction) {
		bookmarkRepository.findByUserAndAuction(user, auction).ifPresent(bookmark -> {
			throw new ValidationException(BookmarkErrorCode.ALREADY_EXISTS_BOOKMARK);
		});
	}

	private Bookmark getBookmarkEntity(User user, Auction auction) {
		return bookmarkRepository.findByUserAndAuction(user, auction)
			.orElseThrow(() -> new ValidationException(BookmarkErrorCode.NOT_FOUND_BOOKMARK));
	}

	private void deleteBookmark(Bookmark bookmark){
		bookmarkRepository.deleteById(bookmark.getId());
	}

}
