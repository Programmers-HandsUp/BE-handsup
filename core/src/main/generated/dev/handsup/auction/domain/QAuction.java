package dev.handsup.auction.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuction is a Querydsl query type for Auction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuction extends EntityPathBase<Auction> {

    private static final long serialVersionUID = -46158260L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuction auction = new QAuction("auction");

    public final dev.handsup.common.entity.QTimeBaseEntity _super = new dev.handsup.common.entity.QTimeBaseEntity(this);

    public final NumberPath<Integer> biddingCount = createNumber("biddingCount", Integer.class);

    public final NumberPath<Integer> bookmarkCount = createNumber("bookmarkCount", Integer.class);

    public final dev.handsup.user.domain.QUser buyer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> initPrice = createNumber("initPrice", Integer.class);

    public final dev.handsup.auction.domain.product.QProduct product;

    public final dev.handsup.user.domain.QUser seller;

    public final EnumPath<dev.handsup.auction.domain.auction_field.AuctionStatus> status = createEnum("status", dev.handsup.auction.domain.auction_field.AuctionStatus.class);

    public final StringPath title = createString("title");

    public final EnumPath<dev.handsup.auction.domain.auction_field.TradeMethod> tradeMethod = createEnum("tradeMethod", dev.handsup.auction.domain.auction_field.TradeMethod.class);

    public final dev.handsup.auction.domain.auction_field.QTradingLocation tradingLocation;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAuction(String variable) {
        this(Auction.class, forVariable(variable), INITS);
    }

    public QAuction(Path<? extends Auction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuction(PathMetadata metadata, PathInits inits) {
        this(Auction.class, metadata, inits);
    }

    public QAuction(Class<? extends Auction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new dev.handsup.user.domain.QUser(forProperty("buyer"), inits.get("buyer")) : null;
        this.product = inits.isInitialized("product") ? new dev.handsup.auction.domain.product.QProduct(forProperty("product"), inits.get("product")) : null;
        this.seller = inits.isInitialized("seller") ? new dev.handsup.user.domain.QUser(forProperty("seller"), inits.get("seller")) : null;
        this.tradingLocation = inits.isInitialized("tradingLocation") ? new dev.handsup.auction.domain.auction_field.QTradingLocation(forProperty("tradingLocation")) : null;
    }

}

