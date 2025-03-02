package com.ampersand.groom.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignInRequest {

    private static final int EMAIL_MAX_LENGTH = 16;
    private static final int PASSWORD_MAX_LENGTH = 30;

    @Email(message = "Invalid email format")
    @Size(max = EMAIL_MAX_LENGTH, message = "Invalid email format")
    private final String email;

    @NotBlank(message = "Invalid password format")
    @Size(max = PASSWORD_MAX_LENGTH, message = "Invalid password format")
    private final String password;

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
