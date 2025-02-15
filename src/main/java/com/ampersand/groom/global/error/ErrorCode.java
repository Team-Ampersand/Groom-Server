package com.ampersand.groom.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    MEMBER_ALREADY_EXISTS("Member already exists", 409),
    MEMBER_NOT_FOUND("Member not found", 404),
    INVALID_OR_EXPIRED("Invalid or expired", 401),
    INVALID_FORMAT("Invalid format", 400);

    private final String message;
    private final int httpStatus;
}