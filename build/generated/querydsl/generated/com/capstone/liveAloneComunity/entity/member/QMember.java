package com.capstone.liveAloneComunity.entity.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -925222308L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.capstone.liveAloneComunity.domain.member.QMemberInfo memberInfo;

    public final com.capstone.liveAloneComunity.domain.member.QPassword password;

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final com.capstone.liveAloneComunity.domain.member.QUsername username;

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberInfo = inits.isInitialized("memberInfo") ? new com.capstone.liveAloneComunity.domain.member.QMemberInfo(forProperty("memberInfo")) : null;
        this.password = inits.isInitialized("password") ? new com.capstone.liveAloneComunity.domain.member.QPassword(forProperty("password")) : null;
        this.username = inits.isInitialized("username") ? new com.capstone.liveAloneComunity.domain.member.QUsername(forProperty("username")) : null;
    }

}
