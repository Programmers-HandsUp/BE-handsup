package dev.handsup.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReviewLabel is a Querydsl query type for ReviewLabel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewLabel extends EntityPathBase<ReviewLabel> {

    private static final long serialVersionUID = 1089533248L;

    public static final QReviewLabel reviewLabel = new QReviewLabel("reviewLabel");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath value = createString("value");

    public QReviewLabel(String variable) {
        super(ReviewLabel.class, forVariable(variable));
    }

    public QReviewLabel(Path<? extends ReviewLabel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReviewLabel(PathMetadata metadata) {
        super(ReviewLabel.class, metadata);
    }

}

