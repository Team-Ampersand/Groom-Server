package com.ampersand.groom.domain.auth.presentation.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class EmailRequest {

    @Email(message = "Invalid email format")
    private final String email;

    public EmailRequest(String email) {
        this.email = email;
    }
}
