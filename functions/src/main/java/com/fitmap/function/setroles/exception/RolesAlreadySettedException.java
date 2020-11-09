package com.fitmap.function.setroles.exception;

import com.fitmap.function.common.exception.TerminalException;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class RolesAlreadySettedException extends TerminalException {

    public RolesAlreadySettedException() {
        super("User already has roles.", HttpStatus.CONFLICT);
    }

}
