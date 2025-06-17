package dev.handsup.auction.repository.auction;

import static dev.handsup.search.domain.QAuctionSearch.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.search.domain.AuctionSearch;
import dev.handsup.search.dto.AuctionSearchCondition;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuctionSearchQueryRepositoryImpl implements AuctionSearchQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<AuctionSearch> searchAuctions(AuctionSearchCondition condition, Pageable pageable) {
		List<AuctionSearch> content = queryFactory.select(auctionSearch)
			.from(auctionSearch)
			.where(
				keywordContains(condition.keyword()),
				categoryEq(condition.productCategory()),
				tradeMethodEq(condition.tradeMethod()),
				siEq(condition.si()),
				guEq(condition.gu()),
				dongEq(condition.dong()),
				initPriceMin(condition.minPrice()),
				initPriceMax(condition.maxPrice()),
				isNewProductEq(condition.isNewProduct()),
				isProgressEq(condition.isProgress())
			)
			.orderBy(auctionSearchSort(pageable))
			.limit(pageable.getPageSize() + 1L)
			.offset(pageable.getOffset())
			.fetch();
		boolean hasNext = hasNext(pageable.getPageSize(), content);
		return new SliceImpl<>(content, pageable, hasNext);
	}

	@Override
	public Slice<AuctionSearch> sortAuctionByCriteria(String si, String gu, String dong, Pageable pageable) {
		List<AuctionSearch> content = queryFactory.select(auctionSearch)
			.from(auctionSearch)
			.where(
				auctionSearch.isProgress.isTrue(),
				siEq(si),
				guEq(gu),
				dongEq(dong)
			)
			.orderBy(auctionSearchSort(pageable))
			.limit(pageable.getPageSize() + 1L)
			.offset(pageable.getOffset())
			.fetch();
		boolean hasNext = hasNext(pageable.getPageSize(), content);
		return new SliceImpl<>(content, pageable, hasNext);
	}

	@Override
	public Slice<AuctionSearch> findByProductCategories(List<String> productCategories, Pageable pageable) {
		List<AuctionSearch> content = queryFactory.select(auctionSearch)
			.from(auctionSearch)
			.where(
				auctionSearch.category.in(productCategories)
			)
			.orderBy(auctionSearch.bookmarkCount.desc())
			.limit(pageable.getPageSize() + 1L)
			.offset(pageable.getOffset())
			.fetch();
		boolean hasNext = hasNext(pageable.getPageSize(), content);
		return new SliceImpl<>(content, pageable, hasNext);
	}

	private OrderSpecifier<?> auctionSearchSort(Pageable pageable) {
		return pageable.getSort().stream()
			.findFirst()
			.map(order -> switch (order.getProperty()) {
				case "북마크수" -> auctionSearch.bookmarkCount.desc();
				case "마감일" -> auctionSearch.endDate.asc();
				case "입찰수" -> auctionSearch.biddingCount.desc();
				default -> auctionSearch.createdAt.desc();
			})
			.orElse(auctionSearch.createdAt.desc()); // 기본값 최신순
	}

	private BooleanExpression keywordContains(String keyword) {
		return keyword != null ? auctionSearch.title.contains(keyword) : null;
	}

	private BooleanExpression categoryEq(String productCategory) {
		return hasText(productCategory) ? auctionSearch.category.eq(productCategory) : null;
	}

	private BooleanExpression tradeMethodEq(String tradeMethod) {
		return hasText(tradeMethod) ? auctionSearch.tradeMethod.eq(TradeMethod.of(tradeMethod)) : null;
	}

	private BooleanExpression siEq(String si) {
		return hasText(si) ? auctionSearch.tradingLocation.si.eq(si) : null;
	}

	private BooleanExpression guEq(String gu) {
		return hasText(gu) ? auctionSearch.tradingLocation.gu.eq(gu) : null;
	}

	private BooleanExpression dongEq(String dong) {
		return hasText(dong) ? auctionSearch.tradingLocation.dong.eq(dong) : null;
	}

	private BooleanExpression initPriceMin(Integer minPrice) {
		return (minPrice != null) ? auctionSearch.currentBiddingPrice.goe(minPrice) : null;
	}

	private BooleanExpression initPriceMax(Integer maxPrice) {
		return (maxPrice != null) ? auctionSearch.currentBiddingPrice.loe(maxPrice) : null;
	}

	private BooleanExpression isNewProductEq(Boolean isNewProduct) {
		if (isNewProduct == null) {
			return null;
		}
		if (isNewProduct) {
			return auctionSearch.isNewProduct.isTrue();
		} else {
			return auctionSearch.isNewProduct.isFalse();
		}
	}

	private BooleanExpression isProgressEq(Boolean isProgress) {
		if (isProgress == null) {
			return null;
		}
		if (isProgress) {
			return auctionSearch.isProgress.isTrue();
		} else {
			return auctionSearch.isProgress.isFalse();
		}
	}

	private boolean hasNext(int pageSize, List<AuctionSearch> auctionSearches) {
		if (auctionSearches.size() <= pageSize) {
			return false;
		}
		auctionSearches.remove(pageSize);
		return true;
	}
}
