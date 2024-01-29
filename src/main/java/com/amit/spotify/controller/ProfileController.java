package com.amit.spotify.controller;

import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.dto.UserDto;
import com.amit.spotify.model.Profile;
import com.amit.spotify.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

@Tag(name = "Profile Controller", description = "Profile controller manage APIs for user profile")
@RestController
@RequestMapping("/v1/user-management/users/{username}/profile")
@CrossOrigin
@Slf4j
public class ProfileController {


    @Autowired
    private ProfileService profileService;


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> fetchProfileByUsername(@PathVariable String username) {
        log.info("Fetching profile for username: {}", username);

        UserDto userDto = profileService.fetchProfileByUsername(username);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> updateProfileByUsername(@PathVariable String username, @RequestBody Profile profile) {
        log.info("Updating profile for username: {} -> {}", username, profile);

        UserDto userDto = profileService.updateProfileByUsername(username, profile);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/profile-image",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<byte[]> fetchProfileImageByUsername(@PathVariable String username) {
        log.info("Fetching profile image for username: {}", username);

        byte[] imageBytes = profileService.fetchProfileImageByUsername(username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/upload-image",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> uploadProfileImageByUsername(
            @PathVariable String username, @RequestParam("image") MultipartFile file) {
        log.info("Updating profile for username: {} -> {}", username, file);
        log.info("Upload image name: {}", file.getName());
        log.info("Upload image filename: {}", file.getOriginalFilename());
        log.info("Upload image content type: {}", file.getContentType());
        log.info("Upload image size: {}", file.getSize());

        UserDto userDto = profileService.uploadProfileImageByUsername(username, file);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/upload-image-db",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> saveProfileImageToDatabaseByUsername(
            @PathVariable String username, @RequestParam("image") MultipartFile file) {
        log.info("Updating profile for username: {} -> {}", username, file);
        log.info("Upload image name: {}", file.getName());
        log.info("Upload image filename: {}", file.getOriginalFilename());
        log.info("Upload image content type: {}", file.getContentType());
        log.info("Upload image size: {}", file.getSize());

        UserDto userDto = profileService.saveProfileImageToDatabaseByUsername(username, file);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/profile-image",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteProfileImageByUsername(@PathVariable String username) {
        log.info("Deleting profile image for username: {}", username);

        String message = profileService.deleteProfileImageByUsername(username);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }



}
