package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCaseWithReadOnlyTransaction
@RequiredArgsConstructor
public class FindAllMembersUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final MemberMapper memberMapper;

    public List<GetMemberResponse> execute() {
        return memberPersistencePort.findAllMembers().stream().map(
                memberMapper::toResponse
        ).toList();
    }
}