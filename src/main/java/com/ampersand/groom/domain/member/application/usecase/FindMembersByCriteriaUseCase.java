package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCaseWithReadOnlyTransaction
@RequiredArgsConstructor
public class FindMembersByCriteriaUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final MemberMapper memberMapper;

    public List<GetMemberResponse> execute(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role) {
        return memberPersistencePort.findMembersByCriteria(id, name, generation, email, isAvailable, role).stream().map(
                memberMapper::toResponse
        ).toList();
    }
}