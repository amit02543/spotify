package com.amit.spotify.controller;

import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.model.Track;
import com.amit.spotify.service.SongService;
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

@Tag(name = "Song Controller", description = "Song controller manages APIs for user liked songs")
@RestController
@RequestMapping("/v1/song-management/users/{username}/songs")
@CrossOrigin
@Slf4j
public class SongController {


    @Autowired
    private SongService songService;


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Track>> fetchLikedSongsByUsername(@PathVariable String username) {
        log.info("Fetching liked songs for username: {}", username);

        List<Track> userSongList = songService.fetchLikedSongsByUsername(username);

        return new ResponseEntity<>(userSongList, HttpStatus.OK);
    }


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Track> addLikedSongToUser(
            @PathVariable String username, @RequestBody Track track) {
        log.info("Adding liked song for username: {}", username);

        Track song = songService.addLikedSongToUser(username, track);

        return new ResponseEntity<>(track, HttpStatus.CREATED);
    }


    @RequestMapping(
            value = "/{songId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Track> fetchLikedSongByUsernameAndId(
            @PathVariable String username, @PathVariable String songId) {
        log.info("Fetching liked song for username: {} with id: {}", username, songId);

        Track track = songService.fetchLikedSongByUsernameAndId(username, songId);

        return new ResponseEntity<>(track, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/{songId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteLikedSongByUsernameAndId(
            @PathVariable String username, @PathVariable String songId) {
        log.info("Deleting liked song for username: {} with id: {}", username, songId);

        String message = songService.deleteLikedSongByUsernameAndId(username, songId);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }




}
