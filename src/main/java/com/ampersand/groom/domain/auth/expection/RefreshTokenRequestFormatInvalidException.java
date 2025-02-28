package com.ampersand.groom.domain.auth.expection;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class RefreshTokenRequestFormatInvalidException extends GroomException {
    public RefreshTokenRequestFormatInvalidException() {
        super(ErrorCode.REFRESH_TOKEN_REQUEST_FORMAT_INVALID);
    }
}
