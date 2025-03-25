package com.ampersand.groom.domain.auth.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class EmailAuthRateLimitException extends GroomException {
    public EmailAuthRateLimitException() {
        super(ErrorCode.EMAIL_AUTHENTICATION_TOO_MANY_REQUESTS);
    }
}