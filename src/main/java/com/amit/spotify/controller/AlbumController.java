package com.amit.spotify.controller;

import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.model.Album;
import com.amit.spotify.service.AlbumService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Album Controller", description = "Album controller manages APIs for user liked albums")
@RestController
@RequestMapping("/v1/album-management/users/{username}/albums")
@CrossOrigin
@Slf4j
public class AlbumController {


    @Autowired
    private AlbumService albumService;


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Album>> fetchLikedAlbumsByUsername(@PathVariable String username) {
        log.info("Fetching liked albums for username: {}", username);

        List<Album> userAlbumList = albumService.fetchLikedAlbumsByUsername(username);

        return new ResponseEntity<>(userAlbumList, HttpStatus.OK);
    }


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Album> addLikedAlbumToUser(
            @PathVariable String username, @RequestBody Album album) {
        log.info("Adding liked album for username: {}", username);

        Album savedAlbum = albumService.addLikedAlbumToUser(username, album);

        return new ResponseEntity<>(savedAlbum, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/{albumId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Album> fetchLikedAlbumByUsernameAndId(
            @PathVariable String username, @PathVariable String albumId) {
        log.info("Fetching liked album for username: {} with id: {}", username, albumId);

        Album album = albumService.fetchLikedAlbumByUsernameAndId(username, albumId);

        return new ResponseEntity<>(album, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/{albumId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteLikedAlbumByUsernameAndId(
            @PathVariable String username, @PathVariable String albumId) {
        log.info("Deleting liked album for username: {} with id: {}", username, albumId);

        String message = albumService.deleteLikedAlbumByUsernameAndId(username, albumId);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }




}
