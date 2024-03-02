package dev.handsup.auction.repository.auction;

import static dev.handsup.auction.domain.QAuction.*;
import static dev.handsup.auction.domain.product.QProduct.*;
import static dev.handsup.auction.domain.product.product_category.QProductCategory.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.domain.QAuction;
import dev.handsup.auction.domain.auction_field.AuctionStatus;
import dev.handsup.auction.domain.auction_field.TradeMethod;
import dev.handsup.auction.domain.product.ProductStatus;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuctionQueryRepositoryImpl implements AuctionQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<Auction> searchAuctions(AuctionSearchCondition condition, Pageable pageable) {
		List<Auction> content = queryFactory.select(QAuction.auction)
			.from(auction)
			.join(auction.product, product).fetchJoin()
			.leftJoin(product.productCategory, productCategory).fetchJoin()
			.where(
				keywordContains(condition.keyword()),
				categoryEq(condition.productCategory()),
				tradeMethodEq(condition.tradeMethod()),
				siEq(condition.si()),
				guEq(condition.gu()),
				dongEq(condition.dong()),
				initPriceBetween(condition.minPrice(), condition.maxPrice()),
				isNewProductEq(condition.isNewProduct()),
				isProgressEq(condition.isProgress())
			)
			.orderBy(auctionSort(pageable))
			.limit(pageable.getPageSize() + 1L)
			.offset(pageable.getOffset())
			.fetch();
		boolean hasNext = hasNext(pageable.getPageSize(), content);
		return new SliceImpl<>(content, pageable, hasNext);
	}

	private OrderSpecifier<?> auctionSort(Pageable pageable) {
		return pageable.getSort().stream()
			.findFirst()
			.map(order -> {
				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
				return switch (order.getProperty()) {
					case "bookmarkCount" -> new OrderSpecifier<>(direction, auction.bookmarkCount);
					case "endDate" -> new OrderSpecifier<>(direction, auction.endDate);
					case "initPrice" -> new OrderSpecifier<>(direction, auction.initPrice);
					default -> auction.createdAt.desc();
				};
			})
			.orElse(auction.createdAt.desc());
	}

	private BooleanExpression keywordContains(String keyword) {
		return keyword != null ? auction.title.contains(keyword) : null;
	}

	private BooleanExpression categoryEq(String productCategory) {
		return hasText(productCategory) ? product.productCategory.categoryValue.eq(productCategory) : null;
	}

	private BooleanExpression tradeMethodEq(String tradeMethod) {
		return hasText(tradeMethod) ? auction.tradeMethod.eq(TradeMethod.of(tradeMethod)) : null;
	}

	private BooleanExpression siEq(String si) {
		return hasText(si) ? auction.tradingLocation.si.eq(si) : null;
	}

	private BooleanExpression guEq(String gu) {
		return hasText(gu) ? auction.tradingLocation.si.eq(gu) : null;
	}

	private BooleanExpression dongEq(String dong) {
		return hasText(dong) ? auction.tradingLocation.si.eq(dong) : null;
	}

	private BooleanExpression initPriceBetween(Integer minPrice, Integer maxPrice) {
		return (minPrice != null && maxPrice != null) ? auction.initPrice.between(minPrice, maxPrice) : null;
	}

	private BooleanExpression isNewProductEq(Boolean isNewProduct) {
		if (isNewProduct == null) {
			return null;
		}
		if (Boolean.TRUE.equals(isNewProduct)) {
			return auction.product.status.eq(ProductStatus.NEW);
		} else {
			return auction.product.status.eq(ProductStatus.CLEAN).or(auction.product.status.eq(ProductStatus.DIRTY));
		}
	}

	private BooleanExpression isProgressEq(Boolean isProgress) {
		if (Boolean.TRUE.equals(isProgress)) {
			return auction.status.eq(AuctionStatus.BIDDING);
		}
		return null;
	}

	private boolean hasNext(int pageSize, List<Auction> auctions) {
		if (auctions.size() <= pageSize) {
			return false;
		}
		auctions.remove(pageSize);
		return true;
	}
}
