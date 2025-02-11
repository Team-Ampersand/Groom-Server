package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.member.application.port.MemberPersistenceReadPort;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCaseWithReadOnlyTransaction
@RequiredArgsConstructor
public class SearchMemberUseCase {

    private final MemberPersistenceReadPort memberPersistenceReadPort;
    private final MemberMapper memberMapper;

    public List<GetMemberResponse> execute(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role) {
        return memberPersistenceReadPort.searchMember(id, name, generation, email, isAvailable, role).stream().map(
                memberMapper::toResponse
        ).toList();
    }
}