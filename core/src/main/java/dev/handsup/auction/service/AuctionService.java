package dev.handsup.auction.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.mapper.AuctionMapper;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionDetailResponse;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.auction.AuctionSearchRepository;
import dev.handsup.auction.repository.product.PreferredProductCategoryRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.search.dto.AuctionSearchMapper;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final AuctionSearchRepository auctionSearchRepository;
	private final ProductCategoryRepository productCategoryRepository;
	private final PreferredProductCategoryRepository preferredProductCategoryRepository;

	public AuctionDetailResponse registerAuction(RegisterAuctionRequest request, User user) {
		ProductCategory productCategory = getProductCategoryByValue(request.productCategory());
		Auction auction = auctionRepository.save(AuctionMapper.toAuction(request, productCategory, user));
		auctionSearchRepository.save(AuctionSearchMapper.toAuctionSearch(auction));

		return AuctionMapper.toAuctionDetailResponse(auction);
	}

	@Transactional(readOnly = true)
	public AuctionDetailResponse getAuctionDetail(Long auctionId) {
		Auction auction = getAuctionById(auctionId);
		return AuctionMapper.toAuctionDetailResponse(auction);
	}

	private ProductCategory getProductCategoryByValue(String productCategoryValue) {
		return productCategoryRepository.findByValue(productCategoryValue)
			.orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY));
	}

	public Auction getAuctionById(Long auctionId) {
		return auctionRepository.findById(auctionId)
			.orElseThrow(() -> new NotFoundException(AuctionErrorCode.NOT_FOUND_AUCTION));
	}
}
