package com.amit.spotify.controller;

import com.amit.spotify.constants.CommonConstants;
import com.amit.spotify.dto.LoginDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@CrossOrigin
@Slf4j
public class LoginController {


    @Autowired
    private LoginService loginService;


    @RequestMapping(
            value = CommonConstants.EMPTY_STR,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> validateUserLogin(@RequestBody LoginDto loginDto) {
        log.info("Login DTO: {}", loginDto);

        try {

            User user = loginService.validateUserLogin(loginDto);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch(SpotifyException e) {
            throw new RuntimeException(e.getMessage());
        }

    }



}
