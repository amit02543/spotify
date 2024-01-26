package com.amit.spotify.controller;

import com.amit.spotify.dto.CollectionDto;
import com.amit.spotify.dto.UserCollectionDto;
import com.amit.spotify.entity.UserAlbum;
import com.amit.spotify.entity.UserCollection;
import com.amit.spotify.entity.UserSong;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.Album;
import com.amit.spotify.model.Track;
import com.amit.spotify.service.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Tag(name = "User Controller", description = "User controller contains APIs for user management")
@RestController
@RequestMapping("/v1/user")
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
        log.info("Fetching collection list for username: {}", username);

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
        log.info("Adding collection for username: {}", username);

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


    @RequestMapping(
            value = "/{username}/collections/{collectionName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> fetchCollectionsByUsernameAndName(
            @PathVariable String username,
            @PathVariable String collectionName) {
        log.info("Fetching {} collection details for username: {}", collectionName, username);

        try {

            List<CollectionDto> userCollectionList = userService.fetchCollectionsByUsernameAndName(username, collectionName);

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
            value = "/{username}/collections/{collectionName}/details",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> fetchCollectionDetailsByUsernameAndName(
            @PathVariable String username,
            @PathVariable String collectionName) {
        log.info("Fetching {} collection details for username: {}", collectionName, username);

        try {

            UserCollection userCollection = userService.fetchCollectionDetailsByUsernameAndName(username, collectionName);

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
            value = "/{username}/collections/{collectionName}/upload",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> uploadCollectionImageByUsernameAndName(
            @PathVariable String username,
            @PathVariable String collectionName,
            @RequestParam("image") MultipartFile file) {
        log.info("uploading image on {} collection for username: {}", collectionName, username);
        log.info("Upload image name: {}", file.getName());
        log.info("Upload image filename: {}", file.getOriginalFilename());
        log.info("Upload image content type: {}", file.getContentType()); 
        log.info("Upload image size: {}", file.getSize());

        try {

            UserCollection userCollection = userService.uploadCollectionImageByUsernameAndName(username, collectionName, file);

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
            value = "/{username}/songs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> fetchLikedSongsByUsername(@PathVariable String username) {
        log.info("Fetching liked songs for username: {}", username);

        try {

            List<UserSong> userSongList = userService.fetchLikedSongsByUsername(username);

            return new ResponseEntity<>(objectMapper.writeValueAsString(userSongList), HttpStatus.OK);
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
            value = "/{username}/songs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> addLikedSongsByUsername(
            @PathVariable String username, @RequestBody Track track) {
        log.info("Adding liked song for username: {}", username);

        try {

            String message = userService.addLikedSongsByUsername(username, track);

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


    @RequestMapping(
            value = "/{username}/albums",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> fetchLikedAlbumsByUsername(@PathVariable String username) {
        log.info("Fetching liked songs for username: {}", username);

        try {

            List<UserAlbum> userSongList = userService.fetchLikedAlbumsByUsername(username);

            return new ResponseEntity<>(objectMapper.writeValueAsString(userSongList), HttpStatus.OK);
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
            value = "/{username}/albums",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> addLikedAlbumsByUsername(
            @PathVariable String username, @RequestBody Album album) {
        log.info("Adding liked song for username: {}", username);

        try {

            String message = userService.addLikedAlbumsByUsername(username, album);

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
