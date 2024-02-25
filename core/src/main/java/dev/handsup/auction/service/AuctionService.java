package dev.handsup.auction.service;

import static dev.handsup.auction.exception.AuctionErrorCode.*;

import org.springframework.stereotype.Service;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.mapper.AuctionMapper;
import dev.handsup.auction.dto.request.RegisterAuctionRequest;
import dev.handsup.auction.dto.response.AuctionResponse;
import dev.handsup.auction.repository.auction.AuctionRepository;
import dev.handsup.auction.repository.product.ProductCategoryRepository;
import dev.handsup.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final ProductCategoryRepository productCategoryRepository;


	public Auction getAuction(Long auctionId) {
		return auctionRepository.findById(auctionId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_AUCTION));
	}

	public AuctionResponse registerAuction(RegisterAuctionRequest request) {
		ProductCategory productCategory = findProductCategoryEntity(request);
		Auction auction = AuctionMapper.toAuction(request, productCategory);
		return AuctionMapper.toAuctionResponse(auctionRepository.save(auction));
	}

	private ProductCategory findProductCategoryEntity(RegisterAuctionRequest request) {
		return productCategoryRepository.findByCategoryValue(request.productCategory()).
			orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_CATEGORY));
	}
}
