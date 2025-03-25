package com.ampersand.groom.domain.auth.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class UserExistException extends GroomException {
    public UserExistException() {
        super(ErrorCode.USER_ALREADY_EXISTS);
    }
}
