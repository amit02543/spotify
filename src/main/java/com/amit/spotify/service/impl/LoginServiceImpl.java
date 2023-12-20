package com.amit.spotify.service.impl;

import com.amit.spotify.constants.CommonConstants;
import com.amit.spotify.dto.LoginDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.repository.UserRepository;
import com.amit.spotify.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private UserRepository userRepository;


    @Override
    public User validateUserLogin(LoginDto loginDto) throws SpotifyException {

        if(null == loginDto.getUsername()) {
            throw new SpotifyException("Username is required");
        } else if(CommonConstants.EMPTY_STR.equals(loginDto.getUsername().trim())) {
            throw new SpotifyException("Username should not be empty");
        } else if(null == loginDto.getPassword()) {
            throw new SpotifyException("Password is required");
        } else if(CommonConstants.EMPTY_STR.equals(loginDto.getPassword())) {
            throw new SpotifyException("Password should not be empty");
        } else if(loginDto.getPassword().length() < 6) {
            throw new SpotifyException("Password should be greater and equal to 6 characters");
        }


        Optional<User> optionalUser = userRepository.findByUsername(loginDto.getUsername());

        if(optionalUser.isEmpty()) {
            throw new SpotifyException("No user found for username: " + loginDto.getUsername());
        }


        User user = optionalUser.get();

        if(!user.getPassword().equals(loginDto.getPassword())) {
            throw new SpotifyException("Password is incorrect");
        }


        return user;
    }


}
