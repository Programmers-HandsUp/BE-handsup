package dev.handsup.bookmark.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookmark is a Querydsl query type for Bookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookmark extends EntityPathBase<Bookmark> {

    private static final long serialVersionUID = -1640794928L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookmark bookmark = new QBookmark("bookmark");

    public final dev.handsup.common.entity.QTimeBaseEntity _super = new dev.handsup.common.entity.QTimeBaseEntity(this);

    public final dev.handsup.auction.domain.QAuction auction;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final dev.handsup.user.domain.QUser user;

    public QBookmark(String variable) {
        this(Bookmark.class, forVariable(variable), INITS);
    }

    public QBookmark(Path<? extends Bookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookmark(PathMetadata metadata, PathInits inits) {
        this(Bookmark.class, metadata, inits);
    }

    public QBookmark(Class<? extends Bookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.auction = inits.isInitialized("auction") ? new dev.handsup.auction.domain.QAuction(forProperty("auction"), inits.get("auction")) : null;
        this.user = inits.isInitialized("user") ? new dev.handsup.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

