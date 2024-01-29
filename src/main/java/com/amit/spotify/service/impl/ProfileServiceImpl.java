package com.amit.spotify.service.impl;

import com.amit.spotify.constants.SpotifyMessageConstants;
import com.amit.spotify.dto.UserDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.Profile;
import com.amit.spotify.repository.UserRepository;
import com.amit.spotify.service.ProfileService;
import com.amit.spotify.util.SpotifyImageUploadUtility;
import com.amit.spotify.util.SpotifyUtility;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private SpotifyImageUploadUtility spotifyImageUploadUtility;


    @Autowired
    private SpotifyUtility spotifyUtility;


    @Override
    public UserDto fetchProfileByUsername(String username) {

        User user = getUserByUsername(username);

        return spotifyUtility.convertUserToUserDto(user);
    }


    @Override
    @Transactional
    public UserDto updateProfileByUsername(String username, Profile profile) {

        User user = getUserByUsername(username);
        user.setName(profile.getName());
        user.setPronoun(profile.getPronoun());
        user.setEmail(profile.getEmail());

        User savedUser = userRepository.save(user);


        return spotifyUtility.convertUserToUserDto(savedUser);
    }


    @Override
    @Transactional
    public UserDto uploadProfileImageByUsername(String username, MultipartFile file) {

        getUserByUsername(username);

        String profileUrl = spotifyImageUploadUtility.uploadImage(file);

        userRepository.updateUserProfileUrlByUsername(username, profileUrl);

        return fetchRefreshedUser(username);
    }


    @Override
    @Transactional
    public UserDto saveProfileImageToDatabaseByUsername(String username, MultipartFile file) {

        User existingUser = getUserByUsername(username);

        try {

            existingUser.setProfileImage(Base64.getEncoder().encodeToString(file.getBytes()));
            userRepository.save(existingUser);

        } catch (IOException e) {
            throw new SpotifyException(
                    SpotifyMessageConstants.IMAGE_DB_SAVE_ERROR_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        return fetchRefreshedUser(username);
    }


    @Override
    public byte[] fetchProfileImageByUsername(String username) {

        User user = getUserByUsername(username);

        String profileImage = user.getProfileImage();


        if(StringUtils.isBlank(profileImage)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.PROFILE_IMAGE_DB_NOT_FOUND_MESSAGE,
                    HttpStatus.NOT_FOUND
            );
        }

        return Base64.getDecoder().decode(profileImage);
    }


    @Override
    @Transactional
    public String deleteProfileImageByUsername(String username) {

        User user = getUserByUsername(username);
        user.setProfileUrl(null);

        userRepository.save(user);


        return "Profile image deleted successfully";
    }


    private User getUserByUsername(String username) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Optional<User> optionalUser = userRepository.findByUsername(username);

        if(optionalUser.isEmpty()) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NOT_FOUND_MESSAGE + username,
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }


        return optionalUser.get();
    }


    private UserDto fetchRefreshedUser(String username) {
        Optional<User> updatedUser = userRepository.findByUsername(username);

        if(updatedUser.isEmpty()) {
            throw new SpotifyException("Something went wrong!!!" + username, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Updated User: {}", updatedUser.get());

        return spotifyUtility.convertUserToUserDto(updatedUser.get());
    }


}
