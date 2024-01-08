package com.amit.spotify.controller;

import com.amit.spotify.dto.UserCollectionDto;
import com.amit.spotify.entity.UserCollection;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @RequestMapping(
            value = "/{username}/collections",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> fetchCollectionsByUsername(@PathVariable String username) {
        log.info("Fetching profile for username: {}", username);

        try {

            List<UserCollection> userCollectionList = userService.fetchCollectionsByUsername(username);

            return new ResponseEntity<>(objectMapper.writeValueAsString(userCollectionList), HttpStatus.OK);
        } catch(SpotifyException e) {

            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("message", e.getMessage());
            errorJsonObject.put("statusCode", e.getStatusCode().value());

            return new ResponseEntity<>(errorJsonObject.toString(), e.getStatusCode());
        } catch (JsonProcessingException e) {

            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("message", "Something went wrong!");
            errorJsonObject.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<>(errorJsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @RequestMapping(
            value = "/{username}/collections",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> addCollectionsByUsername(
            @PathVariable String username,
            @RequestBody UserCollectionDto userCollectionDto) {
        log.info("Fetching profile for username: {}", username);

        try {

            List<UserCollection> userCollectionList = userService.addCollectionsByUsername(userCollectionDto);

            return new ResponseEntity<>(objectMapper.writeValueAsString(userCollectionList), HttpStatus.OK);
        } catch(SpotifyException e) {

            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("message", e.getMessage());
            errorJsonObject.put("statusCode", e.getStatusCode().value());

            return new ResponseEntity<>(errorJsonObject.toString(), e.getStatusCode());
        } catch (JsonProcessingException e) {

            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("message", "Something went wrong!");
            errorJsonObject.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ResponseEntity<>(errorJsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error("Unable to save user collection data: {}", e.getMessage());

        if(e.getMessage().startsWith("Duplicate entry ")) {

            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("message", "Provided collection name is already present for user");
            errorJsonObject.put("statusCode", HttpStatus.UNPROCESSABLE_ENTITY.value());

            return new ResponseEntity<>(errorJsonObject.toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        JSONObject errorJsonObject = new JSONObject();
        errorJsonObject.put("message", "Something went wrong!");
        errorJsonObject.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(errorJsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
