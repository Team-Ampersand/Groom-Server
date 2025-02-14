package com.ampersand.groom.domain.member.presentation.data.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMemberPasswordWebRequest(
        @NotBlank @Size(max = 50) String currentPassword,
        @NotBlank @Size(max = 50) String newPassword
) {
}