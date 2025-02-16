package com.ampersand.groom.domain.auth.presentation.dto;

import lombok.Getter;

@Getter
public class VerificationCodeRequest {

    private final String code;

    public VerificationCodeRequest(String code) {
        this.code = code;
    }
}
