package com.amit.spotify.util;

import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.dto.UserDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

@Component
public class SpotifyUtility {


    public String getFormattedCurrentTimestamp() {

        return Instant
                .ofEpochMilli(new Date().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().format(SpotifyConstants.DATE_TIME_FORMATTER);
    }


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


    public HttpStatus getHttpStatusByCode(HttpStatusCode statusCode) {

        return Arrays.stream(HttpStatus.values())
                .filter(status -> status.value() == statusCode.value())
                .findAny()
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
