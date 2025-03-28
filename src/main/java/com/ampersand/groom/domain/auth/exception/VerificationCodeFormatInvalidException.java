package com.ampersand.groom.domain.auth.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class VerificationCodeFormatInvalidException extends GroomException {
    public VerificationCodeFormatInvalidException() {
        super(ErrorCode.VERIFICATION_CODE_FORMAT_INVALID);
    }
}
