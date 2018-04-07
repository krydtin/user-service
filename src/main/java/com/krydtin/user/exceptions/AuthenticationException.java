package com.krydtin.user.exceptions;

import com.krydtin.user.constant.ErrorCode;

public class AuthenticationException extends RuntimeException {

    private final ErrorCode.Authentication errorCode;
    private final String message;

    public AuthenticationException(ErrorCode.Authentication errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode.Authentication getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
