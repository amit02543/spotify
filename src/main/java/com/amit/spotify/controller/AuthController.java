package com.amit.spotify.controller;

import com.amit.spotify.dto.LoginDto;
import com.amit.spotify.dto.SignUpDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.service.AuthService;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Controller", description = "Auth controller contains APIs for user authentication")
@RestController
@RequestMapping("/v1/auth-management/")
@CrossOrigin
@Slf4j
public class AuthController {


    @Autowired
    private AuthService authService;


    @RequestMapping(
            value = "register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> registerUser(@RequestBody SignUpDto signUpDto) {
        log.info("SignUp DTO: {}", signUpDto);

        User user = authService.registerUser(signUpDto);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @RequestMapping(
            value = "login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<User> validateUserLogin(@RequestBody LoginDto loginDto) {
        log.info("Login DTO: {}", loginDto);

        User user = authService.validateUserLogin(loginDto);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
