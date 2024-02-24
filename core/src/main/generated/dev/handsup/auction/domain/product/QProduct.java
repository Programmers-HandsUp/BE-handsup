package dev.handsup.auction.domain.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1591337465L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final dev.handsup.common.entity.QTimeBaseEntity _super = new dev.handsup.common.entity.QTimeBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final dev.handsup.auction.domain.product.product_category.QProductCategory productCategory;

    public final EnumPath<dev.handsup.auction.domain.auction_field.PurchaseTime> purchaseTime = createEnum("purchaseTime", dev.handsup.auction.domain.auction_field.PurchaseTime.class);

    public final EnumPath<ProductStatus> status = createEnum("status", ProductStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.productCategory = inits.isInitialized("productCategory") ? new dev.handsup.auction.domain.product.product_category.QProductCategory(forProperty("productCategory")) : null;
    }

}

