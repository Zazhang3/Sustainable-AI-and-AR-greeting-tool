package com.tool.exception;

/**
 * wrong password
 */
public class PasswordErrorException extends BaseException {

    public PasswordErrorException(String msg) {
        super(msg);
    }

}
