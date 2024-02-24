package dev.handsup.auction.domain.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductImage is a Querydsl query type for ProductImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductImage extends EntityPathBase<ProductImage> {

    private static final long serialVersionUID = -1605817630L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductImage productImage = new QProductImage("productImage");

    public final dev.handsup.common.entity.QTimeBaseEntity _super = new dev.handsup.common.entity.QTimeBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final QProduct product;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProductImage(String variable) {
        this(ProductImage.class, forVariable(variable), INITS);
    }

    public QProductImage(Path<? extends ProductImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductImage(PathMetadata metadata, PathInits inits) {
        this(ProductImage.class, metadata, inits);
    }

    public QProductImage(Class<? extends ProductImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

