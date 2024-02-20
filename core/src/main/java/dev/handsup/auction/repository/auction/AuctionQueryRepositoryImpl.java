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

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import dev.handsup.auction.domain.Auction;
import dev.handsup.auction.dto.request.AuctionSearchCondition;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuctionQueryRepositoryImpl implements AuctionQueryRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<Auction> findAuctions(AuctionSearchCondition auctionSearchCondition, Pageable pageable) {
		List<Auction> content = queryFactory.select(auction)
			.from(auction)
			.join(auction.product, product)
			.join(product.productCategory, productCategory)
			.where(
				categoryEq(auctionSearchCondition.productCategory())
			)
			.orderBy(auction.createdAt.desc())
			.limit(pageable.getPageSize() + 1L)
			.offset(pageable.getOffset())
			.fetch();

		/*
		select *
		from auction
		join product
		on auction.id = product.id
		where product.productCategoryValue = '디지털 기기'
		 */
		boolean hasNext = hasNext(pageable.getPageSize(), content);
		return new SliceImpl<>(content, pageable, hasNext);
	}

	private BooleanExpression categoryEq(String productCategory){
		return hasText(productCategory) ? product.productCategory.categoryValue.eq(productCategory) : null;
	}
	private boolean hasNext(int pageSize, List<Auction> auctions) {
		if (auctions.size() <= pageSize) {
			return false;
		}
		auctions.remove(pageSize);
		return true;
	}
}
