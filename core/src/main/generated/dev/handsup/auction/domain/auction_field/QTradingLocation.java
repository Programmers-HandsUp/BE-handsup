package dev.handsup.auction.domain.auction_field;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTradingLocation is a Querydsl query type for TradingLocation
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QTradingLocation extends BeanPath<TradingLocation> {

    private static final long serialVersionUID = -2130568881L;

    public static final QTradingLocation tradingLocation = new QTradingLocation("tradingLocation");

    public final StringPath dong = createString("dong");

    public final StringPath gu = createString("gu");

    public final StringPath si = createString("si");

    public QTradingLocation(String variable) {
        super(TradingLocation.class, forVariable(variable));
    }

    public QTradingLocation(Path<? extends TradingLocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTradingLocation(PathMetadata metadata) {
        super(TradingLocation.class, metadata);
    }

}

