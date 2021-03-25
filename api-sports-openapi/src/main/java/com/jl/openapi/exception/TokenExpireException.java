package com.jl.openapi.exception;

public class TokenExpireException extends RuntimeException {

    public TokenExpireException(String msg) {
        super(msg);
    }

}
