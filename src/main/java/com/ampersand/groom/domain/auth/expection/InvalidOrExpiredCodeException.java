package com.ampersand.groom.domain.auth.expection;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class InvalidOrExpiredCodeException extends GroomException {
    public InvalidOrExpiredCodeException() {
        super(ErrorCode.INVALID_OR_EXPIRED);
    }
}
