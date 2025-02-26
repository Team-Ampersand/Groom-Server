package com.ampersand.groom.domain.auth.presentation.data.Request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VerificationCodeRequest {

    @Size(min = 8, max = 8, message = "Invalid format")
    private final String code;

    public VerificationCodeRequest(String code) {
        this.code = code;
    }
}
