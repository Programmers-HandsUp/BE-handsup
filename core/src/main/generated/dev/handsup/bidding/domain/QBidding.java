package dev.handsup.bidding.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBidding is a Querydsl query type for Bidding
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBidding extends EntityPathBase<Bidding> {

    private static final long serialVersionUID = -1637771444L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBidding bidding = new QBidding("bidding");

    public final dev.handsup.common.entity.QTimeBaseEntity _super = new dev.handsup.common.entity.QTimeBaseEntity(this);

    public final dev.handsup.auction.domain.QAuction auction;

    public final dev.handsup.user.domain.QUser bidder;

    public final NumberPath<Integer> biddingPrice = createNumber("biddingPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBidding(String variable) {
        this(Bidding.class, forVariable(variable), INITS);
    }

    public QBidding(Path<? extends Bidding> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBidding(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBidding(PathMetadata metadata, PathInits inits) {
        this(Bidding.class, metadata, inits);
    }

    public QBidding(Class<? extends Bidding> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.auction = inits.isInitialized("auction") ? new dev.handsup.auction.domain.QAuction(forProperty("auction"), inits.get("auction")) : null;
        this.bidder = inits.isInitialized("bidder") ? new dev.handsup.user.domain.QUser(forProperty("bidder"), inits.get("bidder")) : null;
    }

}

