package com.ampersand.groom.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequest {

    @Email(message = "Invalid format")
    private final String email;

    @NotBlank(message = "Password cannot be blank")
    private final String password;

    @NotBlank(message = "Name cannot be blank")
    private final String name;

    public SignupRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
