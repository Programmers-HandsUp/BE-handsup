package dev.handsup.auth.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBlacklistToken is a Querydsl query type for BlacklistToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlacklistToken extends EntityPathBase<BlacklistToken> {

    private static final long serialVersionUID = 941742568L;

    public static final QBlacklistToken blacklistToken = new QBlacklistToken("blacklistToken");

    public final dev.handsup.common.entity.QTimeBaseEntity _super = new dev.handsup.common.entity.QTimeBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath refreshToken = createString("refreshToken");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBlacklistToken(String variable) {
        super(BlacklistToken.class, forVariable(variable));
    }

    public QBlacklistToken(Path<? extends BlacklistToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBlacklistToken(PathMetadata metadata) {
        super(BlacklistToken.class, metadata);
    }

}

