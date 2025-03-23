package com.ampersand.groom.domain.auth.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class EmailFormatInvalidException extends GroomException {
    public EmailFormatInvalidException() {
        super(ErrorCode.EMAIL_FORMAT_INVALID);
    }
}
