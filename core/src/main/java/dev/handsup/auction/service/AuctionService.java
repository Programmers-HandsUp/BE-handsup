package dev.handsup.auction.service;

import static dev.handsup.auction.exception.AuctionErrorCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.Bookmark;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.mapper.AuctionMapper;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionResponse;

import dev.handsup.auction.dto.response.CheckBookmarkStatusResponse;
import dev.handsup.auction.dto.response.EditBookmarkResponse;
import dev.handsup.auction.dto.response.FindUserBookmarkResponse;
import dev.handsup.auction.exception.AuctionErrorCode;

import dev.handsup.auction.repository.auction.AuctionQueryRepository;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.auction.BookmarkRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.common.exception.ValidationException;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final ProductCategoryRepository productCategoryRepository;
	private final AuctionQueryRepository auctionQueryRepository;
	private final BookmarkRepository bookmarkRepository;

	public AuctionResponse registerAuction(RegisterAuctionRequest request) {
		ProductCategory productCategory = findProductCategoryEntity(request);
		Auction auction = AuctionMapper.toAuction(request, productCategory);
		return AuctionMapper.toAuctionResponse(auctionRepository.save(auction));
	}

	@Transactional(readOnly = true)
	public PageResponse<AuctionResponse> searchAuctions(AuctionSearchCondition condition, Pageable pageable) {
		Slice<AuctionResponse> auctionResponsePage = auctionQueryRepository
			.searchAuctions(condition, pageable)
			.map(AuctionMapper::toAuctionResponse);
		return AuctionMapper.toPageResponse(auctionResponsePage);
	}

	@Transactional
	public EditBookmarkResponse addBookmark(User user, Long auctionId){
		Auction auction = getAuctionEntity(auctionId);
		validateIfBookmarkExists(user, auction);
		Bookmark bookmark = AuctionMapper.toBookmark(user, auction);
		auction.increaseBookmarkCount();
		bookmarkRepository.save(bookmark);

		return AuctionMapper.toEditBookmarkResponse(auction.getBookmarkCount());
	}
	@Transactional
	public EditBookmarkResponse cancelBookmark(User user, Long auctionId){
		Auction auction = getAuctionEntity(auctionId);
		deleteBookmark(getBookmarkEntity(user, auction));
		auction.decreaseBookmarkCount();

		return AuctionMapper.toEditBookmarkResponse(auction.getBookmarkCount());
	}

	@Transactional(readOnly = true)
	public CheckBookmarkStatusResponse checkBookmarkStatus(User user, Long auctionId) {
		Auction auction = getAuctionEntity(auctionId);
		boolean isBookmarked= bookmarkRepository.existsByUserAndAuction(user, auction);

		return AuctionMapper.toCheckBookmarkResponse(isBookmarked);
	}

	@Transactional(readOnly = true)
	public PageResponse<FindUserBookmarkResponse> findUserBookmarks(User user, Pageable pageable){
		Slice<FindUserBookmarkResponse> auctionResponsePage
			= auctionRepository.findBookmarkAuction(user, pageable)
			.map(AuctionMapper::toFindUserBookmarkResponse);

		return AuctionMapper.toPageResponse(auctionResponsePage);
	}

	private void validateIfBookmarkExists(User user, Auction auction) {
		bookmarkRepository.findByUserAndAuction(user, auction).ifPresent(bookmark -> {
			throw new ValidationException(AuctionErrorCode.ALREADY_EXISTS_BOOKMARK);
		});
	}

	private Bookmark getBookmarkEntity(User user, Auction auction) {
		return bookmarkRepository.findByUserAndAuction(user, auction)
			.orElseThrow(() -> new ValidationException(AuctionErrorCode.NOT_FOUND_BOOKMARK));
	}

	private ProductCategory findProductCategoryEntity(RegisterAuctionRequest request) {
		return productCategoryRepository.findByCategoryValue(request.productCategory()).
			orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_CATEGORY));
	}

	private void deleteBookmark(Bookmark bookmark){
		bookmarkRepository.deleteById(bookmark.getId());
	}

	public Auction getAuctionEntity(Long auctionId) {
		return auctionRepository.findById(auctionId).
			orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY));
	}
}
