package com.ampersand.groom.domain.member.persistence;

import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.exception.MemberNotFoundException;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.persistence.repository.MemberJpaRepository;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adapter(AdapterType.OUTBOUND)
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberPersistencePort {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberMapper memberMapper;

    @Override
    public List<Member> queryAllMember() {
        return memberJpaRepository.findAll().stream()
                .map(memberMapper::toDomain)
                .toList();
    }

    @Override
    public List<Member> searchMember(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role) {
        return memberJpaRepository.searchMember(id, name, generation, email, isAvailable, role).stream()
                .map(memberMapper::toDomain)
                .toList();
    }

    @Override
    public Member queryMember(Long id) {
        return memberJpaRepository.findById(id)
                .map(memberMapper::toDomain)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public void updateMemberPassword(Long id, String newPassword) {
        memberJpaRepository.updatePassword(id, newPassword);
    }
}