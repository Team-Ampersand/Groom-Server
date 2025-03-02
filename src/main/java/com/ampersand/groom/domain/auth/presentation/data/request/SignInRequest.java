package com.ampersand.groom.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequest {

    @Email(message = "Invalid email format")
    private final String email;

    @NotBlank(message = "Invalid password format")
    private final String password;

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
