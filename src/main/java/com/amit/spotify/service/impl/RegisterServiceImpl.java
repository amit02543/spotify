package com.amit.spotify.service.impl;

import com.amit.spotify.constants.CommonConstants;
import com.amit.spotify.dto.SignUpDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.repository.UserRepository;
import com.amit.spotify.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterServiceImpl implements RegisterService {


    @Autowired
    private UserRepository userRepository;
    
    
    @Override
    public User registerUser(SignUpDto signUpDto) {

        if(null == signUpDto.getUsername()) {
            throw new SpotifyException("Username is required", HttpStatus.BAD_REQUEST);
        } else if(CommonConstants.EMPTY_STR.equals(signUpDto.getUsername().trim())) {
            throw new SpotifyException("Username should not be empty", HttpStatus.BAD_REQUEST);
        } else if(null == signUpDto.getEmail()) {
            throw new SpotifyException("Email is required", HttpStatus.BAD_REQUEST);
        } else if(CommonConstants.EMPTY_STR.equals(signUpDto.getEmail())) {
            throw new SpotifyException("Email should not be empty", HttpStatus.BAD_REQUEST);
        } else if(!signUpDto.getEmail().contains("@") || !signUpDto.getEmail().contains(".")) {
            throw new SpotifyException("Email is not valid", HttpStatus.BAD_REQUEST);
        } else if(null == signUpDto.getPassword()) {
            throw new SpotifyException("Password is required", HttpStatus.BAD_REQUEST);
        } else if(CommonConstants.EMPTY_STR.equals(signUpDto.getPassword())) {
            throw new SpotifyException("Password should not be empty", HttpStatus.BAD_REQUEST);
        } else if(signUpDto.getPassword().length() < 6) {
            throw new SpotifyException("Password should be greater and equal to 6 characters", HttpStatus.BAD_REQUEST);
        } else if(!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
            throw new SpotifyException("Password and confirm password does not match", HttpStatus.BAD_REQUEST);
        }


        Optional<User> optionalUser = userRepository.findByUsername(signUpDto.getUsername());

        if(optionalUser.isPresent()) {
            throw new SpotifyException("User is already registered with username: " + signUpDto.getUsername(), HttpStatus.UNPROCESSABLE_ENTITY);
        }


        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(signUpDto.getPassword());


        try {
            User savedUser = userRepository.save(user);
            return savedUser;
        } catch (DataAccessException e) {
            throw new SpotifyException("Unable to register user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    
}
