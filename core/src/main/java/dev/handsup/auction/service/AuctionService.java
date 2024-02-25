package dev.handsup.auction.service;

import static dev.handsup.auction.exception.AuctionErrorCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.mapper.AuctionMapper;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionDetailResponse;
import dev.handsup.auction.dto.response.AuctionSimpleResponse;
import dev.handsup.auction.repository.auction.AuctionQueryRepository;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final ProductCategoryRepository productCategoryRepository;
	private final AuctionQueryRepository auctionQueryRepository;

	public Auction getAuction(Long auctionId) {
		return auctionRepository.findById(auctionId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_AUCTION));
	}

	public AuctionDetailResponse registerAuction(RegisterAuctionRequest request, User user) {
		ProductCategory productCategory = getProductCategoryEntity(request);
		Auction auction = AuctionMapper.toAuction(request, productCategory, user);
		return AuctionMapper.toAuctionDetailResponse(auctionRepository.save(auction));
	}

	@Transactional(readOnly = true)
	public AuctionDetailResponse getAuctionDetail(Long auctionId) {
		Auction auction = getAuction(auctionId);
		return AuctionMapper.toAuctionDetailResponse(auction);
	}

	@Transactional(readOnly = true)
	public PageResponse<AuctionSimpleResponse> searchAuctions(AuctionSearchCondition condition, Pageable pageable) {
		Slice<AuctionSimpleResponse> auctionResponsePage = auctionQueryRepository
			.searchAuctions(condition, pageable)
			.map(AuctionMapper::toAuctionSimpleResponse);
		return AuctionMapper.toPageResponse(auctionResponsePage);

	}

	private ProductCategory getProductCategoryEntity(RegisterAuctionRequest request) {
		return productCategoryRepository.findByCategoryValue(request.productCategory()).
			orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_CATEGORY));
	}

	public Auction getAuctionEntity(Long auctionId) {
		return auctionRepository.findById(auctionId).
			orElseThrow(() -> new NotFoundException(NOT_FOUND_AUCTION));
	}
}
