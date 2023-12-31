package com.amit.spotify.controller;

import com.amit.spotify.constants.CommonConstants;
import com.amit.spotify.dto.SignUpDto;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.service.RegisterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@CrossOrigin
@Slf4j
public class RegisterController {


    @Autowired
    private RegisterService registerService;


    @Autowired
    private ObjectMapper objectMapper;


    @RequestMapping(
            value = CommonConstants.EMPTY_STR,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> registerUser(@RequestBody SignUpDto signUpDto) {
        log.info("SignUp DTO: {}", signUpDto);

        try {

            registerService.registerUser(signUpDto);

            JSONObject responseObject = new JSONObject();
            responseObject.put("message", "User is register successfully");
            responseObject.put("statusCode", HttpStatus.OK.value());

            return new ResponseEntity<>(responseObject.toString(), HttpStatus.OK);
        } catch(SpotifyException e) {

            JSONObject errorJsonObject = new JSONObject();
            errorJsonObject.put("message", e.getMessage());
            errorJsonObject.put("statusCode", e.getStatusCode().value());

            return new ResponseEntity<>(errorJsonObject.toString(), e.getStatusCode());
        }

    }




}
