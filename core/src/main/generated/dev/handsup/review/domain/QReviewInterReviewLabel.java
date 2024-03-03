package dev.handsup.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewInterReviewLabel is a Querydsl query type for ReviewInterReviewLabel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewInterReviewLabel extends EntityPathBase<ReviewInterReviewLabel> {

    private static final long serialVersionUID = 1733440052L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewInterReviewLabel reviewInterReviewLabel = new QReviewInterReviewLabel("reviewInterReviewLabel");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReview review;

    public final QReviewLabel reviewLabel;

    public QReviewInterReviewLabel(String variable) {
        this(ReviewInterReviewLabel.class, forVariable(variable), INITS);
    }

    public QReviewInterReviewLabel(Path<? extends ReviewInterReviewLabel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewInterReviewLabel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewInterReviewLabel(PathMetadata metadata, PathInits inits) {
        this(ReviewInterReviewLabel.class, metadata, inits);
    }

    public QReviewInterReviewLabel(Class<? extends ReviewInterReviewLabel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
        this.reviewLabel = inits.isInitialized("reviewLabel") ? new QReviewLabel(forProperty("reviewLabel")) : null;
    }

}

