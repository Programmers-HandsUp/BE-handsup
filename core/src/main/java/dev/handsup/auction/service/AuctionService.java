package dev.handsup.auction.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.mapper.AuctionMapper;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionResponse;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionQueryRepository;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final ProductCategoryRepository productCategoryRepository;
	private final AuctionQueryRepository auctionQueryRepository;

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
		return AuctionMapper.toAuctionPageResponse(auctionResponsePage);
	}

	private ProductCategory findProductCategoryEntity(RegisterAuctionRequest request) {
		return productCategoryRepository.findByCategoryValue(request.productCategory()).
			orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY));
	}
}
