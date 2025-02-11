package com.ampersand.groom.global.error.exception;

import com.ampersand.groom.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class GroomException extends RuntimeException {
    private final ErrorCode errorCode;

    public GroomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}