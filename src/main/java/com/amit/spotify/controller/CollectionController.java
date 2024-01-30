package com.amit.spotify.controller;

import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.dto.CollectionDto;
import com.amit.spotify.service.CollectionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Collection Controller", description = "Collection controller manages APIs for collection")
@RequestMapping("/v1/collection-management/collections")
@RestController
@CrossOrigin
@Slf4j
public class CollectionController {


    @Autowired
    private CollectionService collectionService;


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CollectionDto>> fetchAllCollections() {

        List<CollectionDto> collectionList = collectionService.fetchAllCollections();

        return new ResponseEntity<>(collectionList, HttpStatus.OK);
    }


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CollectionDto> addNewCollection(@RequestBody CollectionDto collectionDto) {

        CollectionDto collection = collectionService.addNewCollection(collectionDto);

        return new ResponseEntity<>(collection, HttpStatus.CREATED);
    }


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            params = "filter=byUsername",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CollectionDto>> fetchAllCollectionsItemListByUsername(
            @RequestParam("username") String username) {

        List<CollectionDto> collectionList =
                collectionService.fetchAllCollectionsItemListByUsername(username);

        return new ResponseEntity<>(collectionList, HttpStatus.OK);
    }


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            params = "filter=byNameAndUsername",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<CollectionDto>> fetchCollectionItemListByNameAndUsername(
            @RequestParam("name") String collectionName, @RequestParam("username") String username) {

        List<CollectionDto> collectionList =
                collectionService.fetchCollectionItemListByNameAndUsername(collectionName, username);

        return new ResponseEntity<>(collectionList, HttpStatus.OK);
    }


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            params = "filter=byNameAndUsername",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteCollectionItemListByNameAndUsername(
            @RequestParam("name") String collectionName, @RequestParam("username") String username
    ) {

        String message =
                collectionService.deleteCollectionItemListByNameAndUsername(collectionName, username);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            params = "filter=byNameAndUsernameAndId",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteCollectionItemByNameUsernameAndSpotifyId(
            @RequestParam("name") String collectionName, @RequestParam("username") String username, @RequestParam("id") String id
    ) {

        String message =
                collectionService.deleteCollectionItemByNameUsernameAndSpotifyId(collectionName, username, id);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }




}
