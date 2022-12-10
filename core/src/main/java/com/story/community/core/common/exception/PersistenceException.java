package com.story.community.core.common.exception;

public class PersistenceException extends RuntimeException {

    public PersistenceException(String msg) {
        super(msg);
    }

    public PersistenceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
