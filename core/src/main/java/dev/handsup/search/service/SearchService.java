package dev.handsup.search.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.handsup.auction.dto.mapper.AuctionMapper;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import dev.handsup.auction.dto.response.AuctionResponse;
import dev.handsup.auction.repository.auction.AuctionQueryRepository;
import dev.handsup.auction.repository.search.RedisSearchRepository;
import dev.handsup.common.dto.PageResponse;
import dev.handsup.search.dto.PopularKeywordResponse;
import dev.handsup.search.dto.SearchMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final AuctionQueryRepository auctionQueryRepository;
	private final RedisSearchRepository redisSearchRepository;
	@Transactional(readOnly = true)
	public PageResponse<AuctionResponse> searchAuctions(AuctionSearchCondition condition, Pageable pageable) {
		Slice<AuctionResponse> auctionResponsePage = auctionQueryRepository
			.searchAuctions(condition, pageable)
			.map(AuctionMapper::toAuctionResponse);
		redisSearchRepository.increaseSearchCount(condition.keyword());

		return AuctionMapper.toAuctionPageResponse(auctionResponsePage);
	}

	@Transactional(readOnly = true)
	public PopularKeywordResponse getPopularKeywords() {
		List<String> keywords
			= redisSearchRepository.getPopularKeywords();
		return SearchMapper.toPopularKeywordResponse(keywords);
	}
}
