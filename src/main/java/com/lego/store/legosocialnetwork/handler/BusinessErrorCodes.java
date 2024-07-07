package com.lego.store.legosocialnetwork.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(400, BAD_REQUEST, "Incorrect current password"),
    NEW_PASSWORD_DOES_NOT_MATCH(401, BAD_REQUEST, "New password does not match"),
    ACCOUNT_LOCKED(403, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(404, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(405, FORBIDDEN, "Bad credentials(Login or password is incorrect)"),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatusCode;


    BusinessErrorCodes(int code,  HttpStatus httpStatusCode, String description) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }
}
