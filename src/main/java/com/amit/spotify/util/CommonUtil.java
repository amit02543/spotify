package com.amit.spotify.util;

import com.amit.spotify.dto.UserDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class CommonUtil {


    public UserDto convertUserToUserDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPronoun(user.getPronoun());
        userDto.setProfileUrl(user.getProfileUrl());

        return userDto;
    }


    public String generateSHAHexValue(String input) {

        try {

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(input.getBytes(StandardCharsets.UTF_8));

            byte[] digest = messageDigest.digest();

            StringBuilder hexString = new StringBuilder();

            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new SpotifyException("SHA algorithm is not valid");
        }

    }


}
