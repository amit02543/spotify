package com.amit.spotify.controller;

import com.amit.spotify.dto.CollectionDto;
import com.amit.spotify.entity.Collection;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.service.CollectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Tag(name = "Collection Controller", description = "Collection controller contains APIs for collection management")
@RestController
@CrossOrigin
@Slf4j
public class CollectionController {


    @Autowired
    private CollectionService collectionService;

    @Autowired
    private ObjectMapper objectMapper;


    @RequestMapping(
            value = "/{username}/collections",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> fetchAllCollectionsByUsername(@PathVariable String username) {

        try {

            List<Collection> userCollection = collectionService.fetchAllCollectionsByUsername(username);


            return new ResponseEntity<>(objectMapper.writeValueAsString(userCollection), HttpStatus.OK);
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
            value = "/collections",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> saveCollection(@RequestBody CollectionDto collectionDto) {
        log.info("Adding new collection to database: {}", collectionDto);

        try {

            String message = collectionService.saveCollection(collectionDto);


            JSONObject responseObject = new JSONObject();
            responseObject.put("message", message);
            responseObject.put("statusCode", HttpStatus.OK.value());


            return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
        } catch(SpotifyException e) {

            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("message", e.getMessage());
            errorJsonObject.put("statusCode", e.getStatusCode().value());

            return new ResponseEntity<>(errorJsonObject.toString(), e.getStatusCode());
        }


    }


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error("Unable to save user collection data: {}", e.getMessage());

        if(e.getMessage().startsWith("Duplicate entry ")) {

            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("message", "Album/song already present in user's collection");
            errorJsonObject.put("statusCode", HttpStatus.UNPROCESSABLE_ENTITY.value());

            return new ResponseEntity<>(errorJsonObject.toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        JSONObject errorJsonObject = new JSONObject();
        errorJsonObject.put("message", "Something went wrong!");
        errorJsonObject.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(errorJsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
