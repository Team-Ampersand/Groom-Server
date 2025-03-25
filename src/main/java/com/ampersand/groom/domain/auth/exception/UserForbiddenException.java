package com.ampersand.groom.domain.auth.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class UserForbiddenException extends GroomException {
    public UserForbiddenException() {
        super(ErrorCode.USER_FORBIDDEN);
    }
}
