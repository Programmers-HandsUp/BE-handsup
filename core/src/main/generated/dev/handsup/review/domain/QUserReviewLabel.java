package dev.handsup.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserReviewLabel is a Querydsl query type for UserReviewLabel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserReviewLabel extends EntityPathBase<UserReviewLabel> {

    private static final long serialVersionUID = -1289227659L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserReviewLabel userReviewLabel = new QUserReviewLabel("userReviewLabel");

    public final dev.handsup.common.entity.QTimeBaseEntity _super = new dev.handsup.common.entity.QTimeBaseEntity(this);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReviewLabel reviewLabel;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final dev.handsup.user.domain.QUser user;

    public QUserReviewLabel(String variable) {
        this(UserReviewLabel.class, forVariable(variable), INITS);
    }

    public QUserReviewLabel(Path<? extends UserReviewLabel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserReviewLabel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserReviewLabel(PathMetadata metadata, PathInits inits) {
        this(UserReviewLabel.class, metadata, inits);
    }

    public QUserReviewLabel(Class<? extends UserReviewLabel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reviewLabel = inits.isInitialized("reviewLabel") ? new QReviewLabel(forProperty("reviewLabel")) : null;
        this.user = inits.isInitialized("user") ? new dev.handsup.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

