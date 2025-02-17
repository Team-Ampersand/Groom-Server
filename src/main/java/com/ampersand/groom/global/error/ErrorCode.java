package com.ampersand.groom.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MEMBER_ALREADY_EXISTS("Member already exists", 409),
    MEMBER_NOT_FOUND("Member not found", 404),
    VERIFICATION_CODE_EXPIRED_OR_INVALID("Verification code expired or Invalid", 401),
    EMAIL_FORMAT_INVALID("Email format invalid", 400),
    VERIFICATION_CODE_FORMAT_INVALID("Verification code format invalid", 400);

    private final String message;
    private final int httpStatus;
}