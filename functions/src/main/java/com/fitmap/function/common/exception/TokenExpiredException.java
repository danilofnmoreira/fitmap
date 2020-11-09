package com.fitmap.function.common.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class TokenExpiredException extends TerminalException {

    public TokenExpiredException() {
        super("The IDToken has expired.", HttpStatus.UNAUTHORIZED);
    }

}
