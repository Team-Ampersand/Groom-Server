package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.member.application.port.MemberPersistenceReadPort;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCaseWithReadOnlyTransaction
@RequiredArgsConstructor
public class QueryAllMemberUseCase {

    private final MemberPersistenceReadPort memberPersistenceReadPort;
    private final MemberMapper memberMapper;

    public List<GetMemberResponse> execute() {
        return memberPersistenceReadPort.queryAllMember().stream().map(
                memberMapper::toResponse
        ).toList();
    }
}