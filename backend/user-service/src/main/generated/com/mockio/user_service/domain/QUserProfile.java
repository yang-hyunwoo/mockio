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

    public final com.mockio.common_jpa.domain.QBaseTimeEntity _super = new com.mockio.common_jpa.domain.QBaseTimeEntity(this);

    public final StringPath bio = createString("bio");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keycloakId = createString("keycloakId");

    public final DateTimePath<java.time.OffsetDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.OffsetDateTime.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Long> profileImageId = createNumber("profileImageId", Long.class);

    public final EnumPath<com.mockio.user_service.constant.UserStatus> status = createEnum("status", com.mockio.user_service.constant.UserStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final EnumPath<com.mockio.user_service.constant.ProfileVisibility> visibility = createEnum("visibility", com.mockio.user_service.constant.ProfileVisibility.class);

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

