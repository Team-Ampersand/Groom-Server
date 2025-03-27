package com.ampersand.groom.domain.auth.presentation.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshRequest(
        @NotBlank @Size(max = 256) String refreshToken
) {
}