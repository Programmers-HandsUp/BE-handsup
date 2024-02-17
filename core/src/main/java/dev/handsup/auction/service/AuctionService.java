package dev.handsup.auction.service;

import org.springframework.stereotype.Service;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.product.product_category.ProductCategory;
import dev.handsup.auction.dto.AuctionResponse;
import dev.handsup.auction.dto.RegisterAuctionRequest;
import dev.handsup.auction.dto.mapper.AuctionMapper;
import dev.handsup.auction.exception.AuctionErrorCode;
import dev.handsup.auction.repository.AuctionRepository;
import dev.handsup.auction.repository.ProductCategoryRepository;
import dev.handsup.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionService {
	private final AuctionRepository auctionRepository;
	private final ProductCategoryRepository productCategoryRepository;

	public AuctionResponse registerAuction(RegisterAuctionRequest request) {
		ProductCategory productCategory = findProductCategoryEntity(request);
		Auction auction = AuctionMapper.toAuction(request, productCategory);
		return AuctionMapper.toAuctionResponse(auctionRepository.save(auction));
	}

	private ProductCategory findProductCategoryEntity(RegisterAuctionRequest request) {
		return productCategoryRepository.findByCategoryValue(request.productCategory()).
			orElseThrow(()-> new NotFoundException(AuctionErrorCode.NOT_FOUND_PRODUCT_CATEGORY));
	}
}
