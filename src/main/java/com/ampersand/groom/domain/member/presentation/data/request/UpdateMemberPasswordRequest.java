package com.ampersand.groom.domain.member.presentation.data.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateMemberPasswordRequest {
    public String email;
    public String currentPassword;
    public String newPassword;
}