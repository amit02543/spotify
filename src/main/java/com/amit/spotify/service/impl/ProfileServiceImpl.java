package com.amit.spotify.service.impl;

import com.amit.spotify.config.CloudinaryConfig;
import com.amit.spotify.constants.CommonConstants;
import com.amit.spotify.dto.UserDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.Profile;
import com.amit.spotify.repository.UserRepository;
import com.amit.spotify.service.ProfileService;
import com.amit.spotify.util.CommonUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {


    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private CloudinaryConfig cloudinaryConfig;


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CommonUtil commonUtil;


    @Override
    public UserDto fetchProfileByUsername(String username) {

        if(null == username) {
            throw new SpotifyException("Username is required", HttpStatus.BAD_REQUEST);
        } else if(CommonConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be empty", HttpStatus.BAD_REQUEST);
        }


        Optional<User> optionalUser = userRepository.findByUsername(username);

        if(optionalUser.isEmpty()) {
            throw new SpotifyException("No user found for username: " + username, HttpStatus.UNPROCESSABLE_ENTITY);
        }


        return commonUtil.convertUserToUserDto(optionalUser.get());
    }

    @Override
    @Transactional
    public UserDto updateProfileByUsername(String username, Profile profile) {

        if(null == username) {
            throw new SpotifyException("Username is required", HttpStatus.BAD_REQUEST);
        } else if(CommonConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be empty", HttpStatus.BAD_REQUEST);
        }


        Optional<User> optionalUser = userRepository.findByUsername(username);

        if(optionalUser.isEmpty()) {
            throw new SpotifyException("No user found for username: " + username, HttpStatus.UNPROCESSABLE_ENTITY);
        }


        userRepository.updateUserByUsername(username, profile.getName(), profile.getPronoun(), profile.getEmail());


        Optional<User> updatedUser = userRepository.findByUsername(username);

        if(updatedUser.isEmpty()) {
            throw new SpotifyException("Something went wrong!!!" + username, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return commonUtil.convertUserToUserDto(updatedUser.get());
    }


    @Override
    @Transactional
    public UserDto uploadProfileImageByUsername(String username, MultipartFile file) {

        if(null == username) {
            throw new SpotifyException("Username is required", HttpStatus.BAD_REQUEST);
        } else if(CommonConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be empty", HttpStatus.BAD_REQUEST);
        }


        Optional<User> optionalUser = userRepository.findByUsername(username);

        if(optionalUser.isEmpty()) {
            throw new SpotifyException("No user found for username: " + username, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        //TODO: Upload image to cloudinary

        //TODO: Step-1 generate api url

        String url = String.format(cloudinaryConfig.getImageUrl(), cloudinaryConfig.getCloudName());
        log.info("Upload URL: {}", url);

        //TODO: Step-2 generate signature

        String eager = "w_400,h_300,c_pad|w_260,h_200,c_crop";
        String public_id = UUID.randomUUID().toString();

        long instant = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().getEpochSecond();
        String timestamp = String.valueOf(instant);

        String signatureString = "eager=" + eager + "&public_id=" + public_id + "&timestamp=" + timestamp + cloudinaryConfig.getSecret();
        log.info("Signature string: {}", signatureString);


        String signature;

        try {
            signature = commonUtil.generateSHAHexValue(signatureString);
            log.info("Signature: {}", signature);
        } catch (SpotifyException e) {
            throw new SpotifyException("SHA algorithm is not valid", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        //TODO: Step-3 create request body

        String profileUrl = CommonConstants.EMPTY_STR;

        try {

            byte[] fileContent = file.getBytes();
            String filename = file.getName();

            ContentDisposition contentDisposition = ContentDisposition
                    .builder("form-data")
                    .name("file")
                    .filename(filename)
                    .build();


            MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
            fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());


            HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("file", fileEntity);
            requestBody.put("api_key", List.of(cloudinaryConfig.getKey()));
            requestBody.put("eager", List.of(eager));
            requestBody.put("public_id", List.of(public_id));
            requestBody.put("timestamp", List.of(timestamp));
            requestBody.put("signature", List.of(signature));

            log.info("Request Body: {}", requestBody);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);


            //TODO: Step-4 POST data to cloudinary using RestTemplate

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            log.info("Response Entity: {}", responseEntity);

            if(HttpStatus.OK != responseEntity.getStatusCode()) {
               throw new SpotifyException("Upload failed with status code " + responseEntity.getStatusCode() +
                       " with message: " + responseEntity.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String responseBody = responseEntity.getBody();

            if(null == responseBody) {
                throw new SpotifyException("Response body is null", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("JSON Object: {}", responseBody);

            JSONObject responseJsonObject = new JSONObject(responseBody);
            profileUrl = responseJsonObject.getString("secure_url");

        } catch(IOException e) {
            throw new SpotifyException("Unable to locate file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(RestClientException e) {
            throw new SpotifyException("Unable to upload image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //TODO: Save url to database

        userRepository.updateUserProfileUrlByUsername(username, profileUrl);

        //TODO: Fetch user details and send back to user

        return fetchRefreshedUser(username);
    }


    private UserDto fetchRefreshedUser(String username) {
        Optional<User> updatedUser = userRepository.findByUsername(username);

        if(updatedUser.isEmpty()) {
            throw new SpotifyException("Something went wrong!!!" + username, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Updated User: {}", updatedUser.get());

        return commonUtil.convertUserToUserDto(updatedUser.get());
    }


}
