package com.ampersand.groom.domain.member.presentation.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMemberPasswordRequest(
        @NotBlank @Size(max = 50) String currentPassword,
        @NotBlank @Size(max = 50) String newPassword
) {
}