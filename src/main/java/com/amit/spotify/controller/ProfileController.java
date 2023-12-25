package com.amit.spotify.controller;

import com.amit.spotify.dto.UserDto;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.Profile;
import com.amit.spotify.service.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/profile")
@CrossOrigin
@Slf4j
public class ProfileController {


    @Autowired
    private ProfileService profileService;


    @Autowired
    private ObjectMapper objectMapper;


    @RequestMapping(
            value = "/{username}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> fetchProfileByUsername(@PathVariable String username) {
        log.info("Fetching profile for username: {}", username);

        try {

            UserDto userDto = profileService.fetchProfileByUsername(username);

            return new ResponseEntity<>(objectMapper.writeValueAsString(userDto), HttpStatus.OK);
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
            value = "/{username}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> updateProfileByUsername(@PathVariable String username, @RequestBody Profile profile) {
        log.info("Updating profile for username: {} -> {}", username, profile);

        try {

            UserDto userDto = profileService.updateProfileByUsername(username, profile);

            return new ResponseEntity<>(objectMapper.writeValueAsString(userDto), HttpStatus.OK);
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
            value = "/{username}/upload-image",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> uploadProfileImageByUsername(@PathVariable String username, @RequestParam("image") MultipartFile file) throws IOException {
        log.info("Updating profile for username: {} -> {}", username, file);
        log.info("Upload image name: {}", file.getName());
        log.info("Upload image filename: {}", file.getOriginalFilename());
        log.info("Upload image content type: {}", file.getContentType());
        log.info("Upload image size: {}", file.getSize());

        try {

            UserDto userDto = profileService.uploadProfileImageByUsername(username, file);

            return new ResponseEntity<>(objectMapper.writeValueAsString(userDto), HttpStatus.OK);
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


}
