package dev.handsup.search.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.dto.mapper.AuctionMapper;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.response.AuctionSimpleResponse;
import dev.handsup.auction.repository.auction.AuctionQueryRepository;
import dev.handsup.auction.repository.search.RedisSearchRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.search.dto.PopularKeywordsResponse;
import dev.handsup.search.dto.SearchMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final AuctionQueryRepository auctionQueryRepository;
	private final RedisSearchRepository redisSearchRepository;

	@Transactional(readOnly = true)
	public PageResponse<AuctionSimpleResponse> searchAuctions(AuctionSearchCondition condition, Pageable pageable) {
		Slice<AuctionSimpleResponse> auctionResponsePage = auctionQueryRepository
			.searchAuctions(condition, pageable)
			.map(AuctionMapper::toAuctionSimpleResponse);
		redisSearchRepository.increaseSearchCount(condition.keyword());

		return AuctionMapper.toPageResponse(auctionResponsePage);
	}

	@Transactional(readOnly = true)
	public PopularKeywordsResponse getPopularKeywords() {
		return SearchMapper.toPopularKeywordsResponse(redisSearchRepository.getPopularKeywords(10));
	}
}
