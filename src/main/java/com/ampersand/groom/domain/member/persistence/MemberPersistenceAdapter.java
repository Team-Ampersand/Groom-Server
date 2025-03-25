package com.ampersand.groom.domain.member.persistence;

import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.exception.MemberNotFoundException;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.persistence.repository.MemberJpaRepository;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ampersand.groom.domain.member.persistence.entity.QMemberJpaEntity.memberJpaEntity;

@Adapter(AdapterType.OUTBOUND)
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberPersistencePort {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberMapper memberMapper;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findAllMembers() {
        return memberJpaRepository.findAll().stream()
                .map(memberMapper::toDomain)
                .toList();
    }

    @Override
    public List<Member> findMembersByCriteria(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role) {
        return queryFactory
                .selectFrom(memberJpaEntity)
                .where(
                        id != null ? memberJpaEntity.id.eq(id) : null,
                        name != null ? memberJpaEntity.name.contains(name) : null,
                        generation != null ? memberJpaEntity.generation.eq(generation) : null,
                        email != null ? memberJpaEntity.email.containsIgnoreCase(email) : null,
                        isAvailable != null ? memberJpaEntity.isAvailable.eq(isAvailable) : null,
                        role != null ? memberJpaEntity.role.eq(role) : null
                )
                .fetch()
                .stream()
                .map(memberMapper::toDomain)
                .toList();
    }

    @Override
    public Member findMemberById(Long id) {
        return memberJpaRepository.findById(id)
                .map(memberMapper::toDomain)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public Member findMemberByEmail(String email) {
        return memberJpaRepository.findMemberByEmail(email)
                .map(memberMapper::toDomain)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public List<Member> findMembersByIds(List<Long> ids) {
        return queryFactory
                .selectFrom(memberJpaEntity)
                .where(memberJpaEntity.id.in(ids))
                .fetch()
                .stream()
                .map(memberMapper::toDomain)
                .toList();
    }

    @Override
    public void updateMemberPassword(Long id, String newPassword) {
        queryFactory
                .update(memberJpaEntity)
                .set(memberJpaEntity.password, newPassword)
                .where(memberJpaEntity.id.eq(id))
                .execute();
    }
}