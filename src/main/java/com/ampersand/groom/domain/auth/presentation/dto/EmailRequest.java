package com.ampersand.groom.domain.auth.presentation.dto;

import lombok.Getter;

@Getter
public class EmailRequest {

    private final String email;

    public EmailRequest(String email) {
        this.email = email;
    }
}
