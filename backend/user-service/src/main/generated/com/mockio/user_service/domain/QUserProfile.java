package com.mockio.user_service.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserProfile is a Querydsl query type for UserProfile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserProfile extends EntityPathBase<UserProfile> {

    private static final long serialVersionUID = 505507120L;

    public static final QUserProfile userProfile = new QUserProfile("userProfile");

    public final com.mockio.common_jpa.domain.QBaseEntity _super = new com.mockio.common_jpa.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keycloakId = createString("keycloakId");

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Long> profileImageId = createNumber("profileImageId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public QUserProfile(String variable) {
        super(UserProfile.class, forVariable(variable));
    }

    public QUserProfile(Path<? extends UserProfile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserProfile(PathMetadata metadata) {
        super(UserProfile.class, metadata);
    }

}

