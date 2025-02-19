package com.ampersand.groom.domain.auth.expection;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class VerificationCodeExpiredOrInvalidException extends GroomException {
    public VerificationCodeExpiredOrInvalidException() {
        super(ErrorCode.VERIFICATION_CODE_EXPIRED_OR_INVALID);
    }
}
