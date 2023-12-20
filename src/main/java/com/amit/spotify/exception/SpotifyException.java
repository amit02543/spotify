package com.amit.spotify.exception;

import com.amit.spotify.model.ErrorCode;
import org.springframework.http.HttpStatus;

public class SpotifyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static ErrorCode errorCode;
    private static HttpStatus statusCode;


    public SpotifyException(String message) {
        super(message);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }


    public SpotifyException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }


    public HttpStatus getStatusCode() {
        return statusCode;
    }


    public ErrorCode getErrorCode() {
        return errorCode;
    }


}
