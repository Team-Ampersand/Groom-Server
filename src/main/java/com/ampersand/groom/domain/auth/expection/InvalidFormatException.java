package com.ampersand.groom.domain.auth.expection;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class InvalidFormatException extends GroomException {
    public InvalidFormatException() {
        super(ErrorCode.INVALID_FORMAT);
    }
}
