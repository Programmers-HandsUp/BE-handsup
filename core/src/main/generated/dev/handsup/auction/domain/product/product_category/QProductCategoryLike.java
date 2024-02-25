package dev.handsup.auction.domain.product.product_category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductCategoryLike is a Querydsl query type for ProductCategoryLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductCategoryLike extends EntityPathBase<ProductCategoryLike> {

    private static final long serialVersionUID = -1984657510L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductCategoryLike productCategoryLike = new QProductCategoryLike("productCategoryLike");

    public final dev.handsup.common.entity.QTimeBaseEntity _super = new dev.handsup.common.entity.QTimeBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QProductCategory productCategory;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final dev.handsup.user.domain.QUser user;

    public QProductCategoryLike(String variable) {
        this(ProductCategoryLike.class, forVariable(variable), INITS);
    }

    public QProductCategoryLike(Path<? extends ProductCategoryLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductCategoryLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductCategoryLike(PathMetadata metadata, PathInits inits) {
        this(ProductCategoryLike.class, metadata, inits);
    }

    public QProductCategoryLike(Class<? extends ProductCategoryLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.productCategory = inits.isInitialized("productCategory") ? new QProductCategory(forProperty("productCategory")) : null;
        this.user = inits.isInitialized("user") ? new dev.handsup.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

