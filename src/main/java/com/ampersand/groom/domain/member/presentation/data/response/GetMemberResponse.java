package com.ampersand.groom.domain.member.presentation.data.response;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetMemberResponse {
    private Long id;
    private String name;
    private Integer generation;
    private String email;
    private Boolean isAvailable;
    private MemberRole role;
}