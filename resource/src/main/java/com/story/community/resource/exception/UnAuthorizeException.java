package com.story.community.resource.exception;

import org.springframework.security.core.AuthenticationException;

public class UnAuthorizeException extends AuthenticationException {

    public UnAuthorizeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnAuthorizeException(String msg) {
        super(msg);
    }
}
