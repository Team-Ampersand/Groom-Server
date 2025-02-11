package com.ampersand.groom.domain.member.persistence.mapper;

import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.persistence.entity.MemberJpaEntity;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import com.ampersand.groom.global.mapper.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper implements GenericMapper<MemberJpaEntity, Member> {
    @Override
    public MemberJpaEntity toEntity(Member member) {
        return MemberJpaEntity.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .generation(member.getGeneration())
                .isAvailable(member.getIsAvailable())
                .role(member.getRole())
                .build();
    }

    @Override
    public Member toDomain(MemberJpaEntity jpaEntity) {
        return Member.builder()
                .id(jpaEntity.getId())
                .name(jpaEntity.getName())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .generation(jpaEntity.getGeneration())
                .isAvailable(jpaEntity.getIsAvailable())
                .role(jpaEntity.getRole())
                .build();
    }

    public GetMemberResponse toResponse(Member member) {
        return new GetMemberResponse(
                member.getId(),
                member.getName(),
                member.getGeneration(),
                member.getEmail(),
                member.getIsAvailable(),
                member.getRole()
        );
    }

    // TODO: booking 구현 후 current member api 구현 시 오버로딩 하여 toResponse 메서드 하나 더 구현해야 할것 같습니다
}
