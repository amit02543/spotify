package com.amit.spotify.controller;

import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.service.SpotifyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spotify")
@CrossOrigin
@Slf4j
public class SpotifyController {


    @Autowired
    private SpotifyService spotifyService;


    @Autowired
    private ObjectMapper mapper;


    @RequestMapping(
            value = "/search",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> searchByTermAndType(@RequestParam("query") String query, @RequestParam("type") String type) {

        log.info("Query: {} & Type: {}", query, type);

        try {

            spotifyService.searchByTermAndType(query, type);

            JSONObject responseObject = new JSONObject();
            responseObject.put("message", "User is register successfully");
            responseObject.put("statusCode", HttpStatus.OK.value());

            return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
        } catch(SpotifyException e) {

            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("message", e.getMessage());
            errorJsonObject.put("statusCode", e.getStatusCode().value());

            return new ResponseEntity<>(errorJsonObject.toString(), e.getStatusCode());
        }

    }





}
