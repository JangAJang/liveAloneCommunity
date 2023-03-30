package com.capstone.liveAloneCommunity.domain.post;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCreatedTime is a Querydsl query type for CreatedTime
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCreatedTime extends BeanPath<CreatedTime> {

    private static final long serialVersionUID = -2069621685L;

    public static final QCreatedTime createdTime = new QCreatedTime("createdTime");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public QCreatedTime(String variable) {
        super(CreatedTime.class, forVariable(variable));
    }

    public QCreatedTime(Path<? extends CreatedTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCreatedTime(PathMetadata metadata) {
        super(CreatedTime.class, metadata);
    }

}

