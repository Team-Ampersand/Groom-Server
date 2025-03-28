package com.ampersand.groom.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Size;

public record VerificationCodeRequest(
        @Size(min = 8, max = 8) String code
) {
}