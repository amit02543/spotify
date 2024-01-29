package com.amit.spotify.advice;

import com.amit.spotify.constants.SpotifyMessageConstants;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.ErrorDetails;
import com.amit.spotify.util.SpotifyUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {


    @Autowired
    private SpotifyUtility spotifyUtility;


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDetails> responseStatusExceptionHandler(ResponseStatusException ex,
                                                           WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails()
                .setTimestamp(spotifyUtility.getFormattedCurrentTimestamp())
                .setMessage(ex.getReason())
                .setDetails(request.getDescription(false))
                .setStatus(spotifyUtility.getHttpStatusByCode(ex.getStatusCode()));

        log.error("Response status exception handler: {} -> {}", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(errorDetails, spotifyUtility.getHttpStatusByCode(ex.getStatusCode()));
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException e,
                                                                                  WebRequest request) {

        String errorMessage = SpotifyMessageConstants.GENERIC_ERROR_MESSAGE;

        if(e.getMessage().startsWith("Duplicate entry ")) {
            errorMessage = SpotifyMessageConstants.DUPLICATE_ENTRY_ERROR_MESSAGE;
        }

        ErrorDetails errorDetails = new ErrorDetails()
                .setTimestamp(spotifyUtility.getFormattedCurrentTimestamp())
                .setMessage(errorMessage)
                .setDetails(request.getDescription(false))
                .setStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        log.error("SQL integrity constraint violation exception handler: {} -> {}", e.getMessage(), e.getStackTrace());

        return new ResponseEntity<>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler(SpotifyException.class)
    public ResponseEntity<ErrorDetails> spotifyExceptionHandler(SpotifyException ex,
                                                           WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails()
                .setTimestamp(spotifyUtility.getFormattedCurrentTimestamp())
                .setMessage(ex.getMessage())
                .setDetails(request.getDescription(false))
                .setStatus(ex.getStatusCode());

        log.error("Application custom exception handler: {} -> {}", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
    }


    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<ErrorDetails> badRequestExceptionHandler(HttpClientErrorException.BadRequest ex,
                                                           WebRequest request) {

        final String message = Optional.of(ex.getMessage()).orElse(ex.getClass().getSimpleName());

        ErrorDetails errorDetails = new ErrorDetails()
                .setTimestamp(spotifyUtility.getFormattedCurrentTimestamp())
                .setMessage(message)
                .setDetails(request.getDescription(false))
                .setStatus(HttpStatus.BAD_REQUEST);

        log.error("Bad request exception handler: {} -> {}", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<ErrorDetails> internalServerErrorExceptionHandler(
            HttpServerErrorException.InternalServerError ex, WebRequest request) {

        String message = Optional.of(ex.getMessage()).orElse(ex.getClass().getSimpleName());

        ErrorDetails errorDetails = new ErrorDetails()
                .setTimestamp(spotifyUtility.getFormattedCurrentTimestamp())
                .setMessage(message)
                .setDetails(request.getDescription(false))
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        log.error("Internal server error exception handler: {} -> {}", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDetails> nullPointerExceptionHandler(NullPointerException ex,
                                                           WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails()
                .setTimestamp(spotifyUtility.getFormattedCurrentTimestamp())
                .setMessage(SpotifyMessageConstants.GENERIC_ERROR_MESSAGE)
                .setDetails(request.getDescription(false))
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        log.error("Null pointer exception handler: {} -> {}", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetails> runtimeExceptionHandler(RuntimeException ex,
                                                           WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails()
                .setTimestamp(spotifyUtility.getFormattedCurrentTimestamp())
                .setMessage(SpotifyMessageConstants.GENERIC_ERROR_MESSAGE)
                .setDetails(request.getDescription(false))
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        log.error("Runtime exception handler: {} -> {}", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> globalExceptionHandler(Exception ex,
                                                           WebRequest request) {
        final String message = Optional.of(ex.getMessage()).orElse(ex.getClass().getSimpleName());

        ErrorDetails errorDetails = new ErrorDetails()
                .setTimestamp(spotifyUtility.getFormattedCurrentTimestamp())
                .setMessage(message)
                .setDetails(request.getDescription(false))
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        log.error("Global exception handler: {} -> {}", ex.getMessage(), ex.getStackTrace());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}