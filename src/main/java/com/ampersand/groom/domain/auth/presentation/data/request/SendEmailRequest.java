package com.ampersand.groom.domain.auth.presentation.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record SendEmailRequest(
        @Email @Size(max = 16) String email
) {
}