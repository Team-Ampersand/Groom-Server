package com.ampersand.groom.domain.member.presentation.data.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateMemberPasswordWebRequest {
    @Size(min = 16, max = 16)
    @Email
    @NotBlank
    public String email;
    @NotBlank
    @Size(max = 50)
    public String currentPassword;
    @NotBlank
    @Size(max = 50)
    public String newPassword;
}
