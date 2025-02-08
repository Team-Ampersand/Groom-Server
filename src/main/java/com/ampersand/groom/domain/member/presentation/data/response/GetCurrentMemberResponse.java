package com.ampersand.groom.domain.member.presentation.data.response;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetCurrentMemberResponse {
    private UUID uuid;
    private String name;
    private Integer generation;
    private String email;
    private Boolean isAvailable;
    private MemberRole role;
    private List<?> booked; // TODO: booking 관련 기능 구현 시 변경
}