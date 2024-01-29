package com.amit.spotify.service.impl;

import com.amit.spotify.constants.SpotifyMessageConstants;
import com.amit.spotify.dto.LoginDto;
import com.amit.spotify.dto.SignUpDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.repository.UserRepository;
import com.amit.spotify.service.AuthService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional
    public User registerUser(SignUpDto signUpDto) {

        if(StringUtils.isBlank(signUpDto.getUsername())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(signUpDto.getEmail())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.EMAIL_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(signUpDto.getPassword())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.PASSWORD_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(!signUpDto.getEmail().contains("@") || !signUpDto.getEmail().contains(".")) {
            throw new SpotifyException(
                    SpotifyMessageConstants.EMAIL_NOT_VALID_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(signUpDto.getPassword().length() < 6) {
            throw new SpotifyException(
                    SpotifyMessageConstants.PASSWORD_LENGTH_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.PASSWORD_NOT_MATCHED_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Optional<User> optionalUser = userRepository.findByUsername(signUpDto.getUsername());

        if(optionalUser.isPresent()) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_ALREADY_FOUND_MESSAGE + signUpDto.getUsername(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }


        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(signUpDto.getPassword());


        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USER_REGISTER_FAILED_MESSAGE + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }


    @Override
    @Transactional
    public User validateUserLogin(LoginDto loginDto) {

        if(StringUtils.isBlank(loginDto.getUsername())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(loginDto.getPassword())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.PASSWORD_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(loginDto.getPassword().length() < 6) {
            throw new SpotifyException(
                    SpotifyMessageConstants.PASSWORD_LENGTH_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Optional<User> optionalUser = userRepository.findByUsername(loginDto.getUsername());

        if(optionalUser.isEmpty()) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NOT_FOUND_MESSAGE + loginDto.getUsername(),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }


        User user = optionalUser.get();

        if(!user.getPassword().equals(loginDto.getPassword())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.PASSWORD_INCORRECT_MESSAGE,
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }


        return user;
    }


}
