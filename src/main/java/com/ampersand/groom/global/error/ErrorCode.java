package com.ampersand.groom.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // MEMBER
    MEMBER_ALREADY_EXISTS("Member already exists", 409),
    MEMBER_NOT_FOUND("Member not found", 404),

    // BOOKING
    INVALID_BOOKING_PARTICIPANTS("Invalid booking participants", 400),
    INVALID_BOOKING_INFORMATION("Invalid booking information", 400),
    DUPLICATE_BOOKING("Duplicate booking", 409),
    MAX_CAPACITY_EXCEEDED("Max capacity exceeded", 400),
    NOT_BOOKING_PRESIDENT("Not booking president", 403),
    NOT_BOOKING_DATE("Not booking date", 403),

    // EMAIL AUTHENTICATION
    VERIFICATION_CODE_EXPIRED_OR_INVALID("Verification code expired or invalid", 401),
    EMAIL_FORMAT_INVALID("Email format invalid", 400),
    VERIFICATION_CODE_FORMAT_INVALID("Verification code format invalid", 400),

    PASSWORD_INVALID("Password invalid", 401),
    USER_ALREADY_EXISTS("User already exists", 409),
    USER_NOT_FOUND("User not found", 404),
    USER_FORBIDDEN("Email verification is not complete", 403),
    EMAIL_OR_PASSWORD_EMPTY("email or password is empty", 400),
    REFRESH_TOKEN_EXPIRED_OR_INVALID("Refresh token expired or invalid", 401),
    REFRESH_TOKEN_REQUEST_FORMAT_INVALID("Refresh token request format invalid", 400);


    private final String message;
    private final int httpStatus;
}