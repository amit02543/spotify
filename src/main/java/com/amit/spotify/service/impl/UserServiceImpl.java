package com.amit.spotify.service.impl;

import com.amit.spotify.constants.SpotifyMessageConstants;
import com.amit.spotify.dto.UserDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.repository.UserRepository;
import com.amit.spotify.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDto fetchUserByUsername(String username) {

        User user = findUserByUsername(username);

        return convertUserToUserDto(user);
    }


    @Override
    @Transactional
    public String deleteUserByUsername(String username) {

        User user = findUserByUsername(username);

        userRepository.deleteById(user.getId());

        return String.format("%s user is deleted successfully", username);
    }


    private User findUserByUsername(String username) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Optional<User> optionalUser = userRepository.findByUsername(username);

        if(optionalUser.isEmpty()) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NOT_FOUND_MESSAGE,
                    HttpStatus.NOT_FOUND
            );
        }


        return optionalUser.get();
    }


    private UserDto convertUserToUserDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setProfileUrl(user.getProfileUrl());
        userDto.setPronoun(user.getPronoun());


        return userDto;
    }


}
