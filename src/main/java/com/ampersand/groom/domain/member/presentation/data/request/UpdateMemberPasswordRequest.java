package com.ampersand.groom.domain.member.presentation.data.request;

public record UpdateMemberPasswordRequest(String currentPassword, String newPassword) {
}