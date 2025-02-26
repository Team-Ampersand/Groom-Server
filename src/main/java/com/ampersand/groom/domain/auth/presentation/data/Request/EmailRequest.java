package com.ampersand.groom.domain.auth.presentation.data.Request;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class EmailRequest {

    @Email(message = "Invalid format")
    private final String email;

    public EmailRequest(String email) {
        this.email = email;
    }
}
