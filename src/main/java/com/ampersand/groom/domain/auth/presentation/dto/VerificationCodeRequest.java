package com.ampersand.groom.domain.auth.presentation.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VerificationCodeRequest {

    @Size(min = 8, max = 8, message = "Verification code must be 8 digits")
    private final String code;

    public VerificationCodeRequest(String code) {
        this.code = code;
    }
}
