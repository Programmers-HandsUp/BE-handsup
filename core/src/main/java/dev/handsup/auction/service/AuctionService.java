package dev.handsup.auction.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.PreferredProductCategory;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.mapper.AuctionMapper;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionDetailResponse;
import dev.handsup.auction.dto.response.RecommendAuctionResponse;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.auction.AuctionQueryRepository;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.PreferredProductCategoryRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.dto.CommonMapper;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.common.exception.NotFoundException;
import dev.handsup.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final ProductCategoryRepository productCategoryRepository;
	private final PreferredProductCategoryRepository preferredProductCategoryRepository;
	private final AuctionQueryRepository auctionQueryRepository;

	public AuctionDetailResponse registerAuction(RegisterAuctionRequest request, User user) {
		ProductCategory productCategory = getProductCategoryByValue(request.productCategory());
		Auction auction = AuctionMapper.toAuction(request, productCategory, user);
		return AuctionMapper.toAuctionDetailResponse(auctionRepository.save(auction));
	}

	@Transactional(readOnly = true)
	public AuctionDetailResponse getAuctionDetail(Long auctionId) {
		Auction auction = getAuctionById(auctionId);
		return AuctionMapper.toAuctionDetailResponse(auction);
	}

	@Transactional(readOnly = true)
	public PageResponse<RecommendAuctionResponse> getRecommendAuctions(String si, String gu, String dong,
		Pageable pageable) {
		Slice<RecommendAuctionResponse> auctionResponsePage = auctionQueryRepository
			.sortAuctionByCriteria(si, gu, dong, pageable)
			.map(AuctionMapper::toRecommendAuctionResponse);
		return CommonMapper.toPageResponse(auctionResponsePage);
	}

	@Transactional(readOnly = true)
	public PageResponse<RecommendAuctionResponse> getUserPreferredCategoryAuctions(User user, Pageable pageable) {
		List<ProductCategory> productCategories = preferredProductCategoryRepository.findByUser(user)
			.stream()
			.map(PreferredProductCategory::getProductCategory)
			.toList();

		Slice<RecommendAuctionResponse> auctionResponsePage = auctionQueryRepository
			.findByProductCategories(productCategories, pageable)
			.map(AuctionMapper::toRecommendAuctionResponse);

		return CommonMapper.toPageResponse(auctionResponsePage);
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
