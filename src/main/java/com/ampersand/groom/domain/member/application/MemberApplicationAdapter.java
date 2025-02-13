package com.ampersand.groom.domain.member.application;

import com.ampersand.groom.domain.member.application.port.MemberApplicationPort;
import com.ampersand.groom.domain.member.application.usecase.FindAllMembersUseCase;
import com.ampersand.groom.domain.member.application.usecase.FindMembersByCriteriaUseCase;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.presentation.data.response.GetCurrentMemberResponse;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adapter(AdapterType.INBOUND)
@RequiredArgsConstructor
public class MemberApplicationAdapter implements MemberApplicationPort {

    private final FindAllMembersUseCase findAllMembersUseCase;
    private final FindMembersByCriteriaUseCase findMembersByCriteriaUseCase;

    @Override
    public List<GetMemberResponse> getAllMembers() {
        return findAllMembersUseCase.execute();
    }

    @Override
    public List<GetMemberResponse> searchMembers(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role) {
        return findMembersByCriteriaUseCase.execute(id, name, generation, email, isAvailable, role);
    }

    @Override
    public GetCurrentMemberResponse getCurrentMember() {
        return null;  // TODO: 인증/인가 및 booking 관련 로직 구현 시 구현
    }

    @Override
    public void updatePassword(Long id, String currentPassword, String newPassword) {
        // TODO: 인증/인가 및 Email 전송 로직 구현 시 구현
    }
}
