package com.ampersand.groom.domain.auth.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class RefreshTokenExpiredOrInvalidException extends GroomException {
    public RefreshTokenExpiredOrInvalidException() {
        super(ErrorCode.REFRESH_TOKEN_EXPIRED_OR_INVALID);
    }
}
