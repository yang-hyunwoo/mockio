package com.mockio.user_service.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserInterviewPreference is a Querydsl query type for UserInterviewPreference
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserInterviewPreference extends EntityPathBase<UserInterviewPreference> {

    private static final long serialVersionUID = -1956180413L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserInterviewPreference userInterviewPreference = new QUserInterviewPreference("userInterviewPreference");

    public final com.mockio.common_jpa.domain.QBaseTimeEntity _super = new com.mockio.common_jpa.domain.QBaseTimeEntity(this);

    public final NumberPath<Integer> answerTimeSeconds = createNumber("answerTimeSeconds", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.mockio.user_service.constant.InterviewDifficulty> difficulty = createEnum("difficulty", com.mockio.user_service.constant.InterviewDifficulty.class);

    public final EnumPath<com.mockio.user_service.constant.FeedbackStyle> feedbackStyle = createEnum("feedbackStyle", com.mockio.user_service.constant.FeedbackStyle.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.mockio.user_service.constant.InterviewMode> interviewMode = createEnum("interviewMode", com.mockio.user_service.constant.InterviewMode.class);

    public final EnumPath<com.mockio.user_service.constant.InterviewTrack> track = createEnum("track", com.mockio.user_service.constant.InterviewTrack.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUserProfile userProfile;

    public QUserInterviewPreference(String variable) {
        this(UserInterviewPreference.class, forVariable(variable), INITS);
    }

    public QUserInterviewPreference(Path<? extends UserInterviewPreference> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserInterviewPreference(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserInterviewPreference(PathMetadata metadata, PathInits inits) {
        this(UserInterviewPreference.class, metadata, inits);
    }

    public QUserInterviewPreference(Class<? extends UserInterviewPreference> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userProfile = inits.isInitialized("userProfile") ? new QUserProfile(forProperty("userProfile")) : null;
    }

}

