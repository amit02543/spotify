package com.amit.spotify.controller;

import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.dto.UserDto;
import com.amit.spotify.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Controller", description = "User controller manages APIs for user management")
@RestController
@RequestMapping("/v1/user-management/users/{username}")
@CrossOrigin
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> fetchUserByUsername(@PathVariable String username) {

        UserDto userDto = userService.fetchUserByUsername(username);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    @RequestMapping(
            value = SpotifyConstants.EMPTY_STR,
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {

        String message = userService.deleteUserByUsername(username);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


}
