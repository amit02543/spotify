package com.amit.spotify.controller;

import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.dto.UserCollectionDto;
import com.amit.spotify.model.UserCollection;
import com.amit.spotify.service.UserCollectionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "User Collection Controller", description = "User Collection controller manages APIs for user collections")
@RestController
@RequestMapping("/v1/user-collection-management/users/{username}/collections")
@CrossOrigin
@Slf4j
public class UserCollectionController {


    @Autowired
    private UserCollectionService userCollectionService;


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<UserCollection>> fetchAllCollectionsByUsername(@PathVariable String username) {
        log.info("Fetching all collections for username: {}", username);

        List<UserCollection> userCollectionList = userCollectionService.fetchAllCollectionsByUsername(username);

        return new ResponseEntity<>(userCollectionList, HttpStatus.OK);
    }


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserCollection> addUserCollection(
            @PathVariable String username, @RequestBody UserCollectionDto userCollectionDto) {
        log.info("Adding collection for username: {}", username);

        UserCollection userCollection = userCollectionService.addUserCollection(userCollectionDto);

        return new ResponseEntity<>(userCollection, HttpStatus.CREATED);
    }


    @RequestMapping(
            value = "/{collectionName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserCollection> fetchCollectionItemListByUsernameAndName(
            @PathVariable String username, @PathVariable String collectionName) {
        log.info("Fetching collection items for username: {} with name: {}", username, collectionName);

        UserCollection userCollection = userCollectionService.fetchCollectionByUsernameAndName(username, collectionName);

        return new ResponseEntity<>(userCollection, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/{collectionName}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserCollection> updateCollectionByUsernameAndName(
            @PathVariable String username, @PathVariable String collectionName, @RequestBody UserCollection userCollection) {
        log.info("Updating collection for username: {} with name: {}", username, collectionName);

        UserCollection updatedUserCollection = userCollectionService.updateCollectionByUsernameAndName(username, collectionName, userCollection);

        return new ResponseEntity<>(updatedUserCollection, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/{collectionName}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteCollectionByUsernameAndName(
            @PathVariable String username, @PathVariable String collectionName) {
        log.info("Deleting collection for username: {} with name: {}", username, collectionName);

        String message = userCollectionService.deleteCollectionByUsernameAndName(username, collectionName);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/{collectionName}/upload-image",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserCollection> uploadCollectionImageByUsernameAndName(
            @PathVariable String username, @PathVariable String collectionName, @RequestParam("image") MultipartFile file) {
        log.info("Upload collection image for username: {} with collection name: {}", username, collectionName);

        UserCollection updatedUserCollection = userCollectionService.uploadCollectionImageByUsernameAndName(username, collectionName, file);

        return new ResponseEntity<>(updatedUserCollection, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/{collectionName}/image",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteCollectionImageByUsernameAndName(
            @PathVariable String username, @PathVariable String collectionName) {
        log.info("Deleting collection image for username: {} with collection name: {}", username, collectionName);

        String message = userCollectionService.deleteCollectionImageByUsernameAndName(username, collectionName);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }



}
