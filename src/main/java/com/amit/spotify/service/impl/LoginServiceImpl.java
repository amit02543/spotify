package com.amit.spotify.service.impl;

import com.amit.spotify.constants.CommonConstants;
import com.amit.spotify.dto.LoginDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.repository.UserRepository;
import com.amit.spotify.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private UserRepository userRepository;


    @Override
    public User validateUserLogin(LoginDto loginDto) throws SpotifyException {

        if(null == loginDto.getUsername()) {
            throw new SpotifyException("Username is required", HttpStatus.BAD_REQUEST);
        } else if(CommonConstants.EMPTY_STR.equals(loginDto.getUsername().trim())) {
            throw new SpotifyException("Username should not be empty", HttpStatus.BAD_REQUEST);
        } else if(null == loginDto.getPassword()) {
            throw new SpotifyException("Password is required", HttpStatus.BAD_REQUEST);
        } else if(CommonConstants.EMPTY_STR.equals(loginDto.getPassword())) {
            throw new SpotifyException("Password should not be empty", HttpStatus.BAD_REQUEST);
        } else if(loginDto.getPassword().length() < 6) {
            throw new SpotifyException("Password should be greater and equal to 6 characters", HttpStatus.BAD_REQUEST);
        }


        Optional<User> optionalUser = userRepository.findByUsername(loginDto.getUsername());

        if(optionalUser.isEmpty()) {
            throw new SpotifyException("No user found for username: " + loginDto.getUsername(), HttpStatus.UNPROCESSABLE_ENTITY);
        }


        User user = optionalUser.get();

        if(!user.getPassword().equals(loginDto.getPassword())) {
            throw new SpotifyException("Password is incorrect", HttpStatus.UNPROCESSABLE_ENTITY);
        }


        return user;
    }


}
