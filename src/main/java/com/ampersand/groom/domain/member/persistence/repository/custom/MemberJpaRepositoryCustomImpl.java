package com.ampersand.groom.domain.member.persistence.repository.custom;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.persistence.entity.MemberJpaEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ampersand.groom.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepositoryCustomImpl implements MemberJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public void updatePassword(Long id, String newPassword) {
        queryFactory
                .update(memberJpaEntity)
                .set(memberJpaEntity.password, newPassword)
                .where(memberJpaEntity.id.eq(id))
                .execute();
    }

    @Override
    public List<MemberJpaEntity> findMembersByIds(List<Long> ids) {
        return queryFactory
                .selectFrom(memberJpaEntity)
                .where(memberJpaEntity.id.in(ids))
                .fetch();
    }

    @Override
    public List<MemberJpaEntity> findMembersByCriteria(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role) {
        return queryFactory
                .selectFrom(memberJpaEntity)
                .where(
                        eqId(id),
                        containsName(name),
                        eqGeneration(generation),
                        containsEmail(email),
                        eqIsAvailable(isAvailable),
                        eqRole(role)
                )
                .fetch();
    }

    @Override
    public Optional<MemberJpaEntity> findMemberByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(memberJpaEntity)
                        .where(memberJpaEntity.email.eq(email))
                        .fetchOne());
    }

    private BooleanExpression eqId(Long id) {
        return id != null ? memberJpaEntity.id.eq(id) : null;
    }

    private BooleanExpression containsName(String name) {
        return name != null ? memberJpaEntity.name.contains(name) : null;
    }

    private BooleanExpression eqGeneration(Integer generation) {
        return generation != null ? memberJpaEntity.generation.eq(generation) : null;
    }

    private BooleanExpression containsEmail(String email) {
        return email != null ? memberJpaEntity.email.containsIgnoreCase(email) : null;
    }

    private BooleanExpression eqIsAvailable(Boolean isAvailable) {
        return isAvailable != null ? memberJpaEntity.isAvailable.eq(isAvailable) : null;
    }

    private BooleanExpression eqRole(MemberRole role) {
        return role != null ? memberJpaEntity.role.eq(role) : null;
    }
}