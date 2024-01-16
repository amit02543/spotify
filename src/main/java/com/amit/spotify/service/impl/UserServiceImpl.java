package com.amit.spotify.service.impl;

import com.amit.spotify.config.CloudinaryConfig;
import com.amit.spotify.constants.CommonConstants;
import com.amit.spotify.dto.CollectionDto;
import com.amit.spotify.dto.UserCollectionDto;
import com.amit.spotify.entity.Collection;
import com.amit.spotify.entity.UserCollection;
import com.amit.spotify.entity.UserSong;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.Track;
import com.amit.spotify.repository.CollectionRepository;
import com.amit.spotify.repository.UserCollectionRepository;
import com.amit.spotify.repository.UserSongRepository;
import com.amit.spotify.service.UserService;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private CloudinaryConfig cloudinaryConfig;


    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private CollectionRepository collectionRepository;


    @Autowired
    private UserCollectionRepository userCollectionRepository;


    @Autowired
    private UserSongRepository userSongRepository;


    @Autowired
    private CommonUtil commonUtil;


    @Override
    public List<UserCollection> fetchCollectionsByUsername(String username) {

        if(null == username || CommonConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be null or empty", HttpStatus.BAD_REQUEST);
        }

        return userCollectionRepository.findAllByUsername(username);
    }

    @Transactional
    @Override
    public List<UserCollection> addCollectionsByUsername(UserCollectionDto userCollectionDto) {

        if(null == userCollectionDto.getUserName() || CommonConstants.EMPTY_STR.equals(userCollectionDto.getUserName().trim())) {
            throw new SpotifyException("Username can not be null or empty", HttpStatus.BAD_REQUEST);
        } else if(null == userCollectionDto.getCollectionName() || CommonConstants.EMPTY_STR.equals(userCollectionDto.getCollectionName().trim())) {
            throw new SpotifyException("Collection name can not be null or empty", HttpStatus.BAD_REQUEST);
        }

        UserCollection userCollection = new UserCollection();
        userCollection.setUserName(userCollectionDto.getUserName());
        userCollection.setName(userCollectionDto.getCollectionName());
        userCollection.setCreatedDate(LocalDateTime.now());
        userCollection.setLastUpdatedDate(LocalDateTime.now());


        userCollectionRepository.save(userCollection);


        return fetchCollectionsByUsername(userCollectionDto.getUserName());
    }


    @Override
    public List<UserSong> fetchUserLikedSongsByUsername(String username) {

        if(null == username || CommonConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be null or empty", HttpStatus.BAD_REQUEST);
        }

        return userSongRepository.findAllSongsByUsername(username);
    }


    @Override
    @Transactional
    public String addUserLikedSongsByUsername(String username, Track track) {

        if(null == username || CommonConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be null or empty", HttpStatus.BAD_REQUEST);
        } else if(null == track.getId() || CommonConstants.EMPTY_STR.equals(track.getId().trim())) {
            throw new SpotifyException("Track id can not be null or empty", HttpStatus.BAD_REQUEST);
        } else if(null == track.getTitle() || CommonConstants.EMPTY_STR.equals(track.getTitle().trim())) {
            throw new SpotifyException("Track title can not be null or empty", HttpStatus.BAD_REQUEST);
        }


        UserSong song = new UserSong();
        song.setUsername(username);
        song.setTrackId(track.getId());
        song.setTitle(track.getTitle());
        song.setArtists(track.getArtists());
        song.setAlbum(track.getAlbum());
        song.setReleaseDate(track.getReleaseDate());
        song.setDuration(track.getDuration());
        song.setImageUrl(track.getImageUrl());
        song.setLikedDate(LocalDateTime.now());


        userSongRepository.save(song);


        return "Song added successfully";
    }

    @Override
    public List<CollectionDto> fetchCollectionsByUsernameAndName(String username, String collectionName) {

        if(null == username || CommonConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be null or empty", HttpStatus.BAD_REQUEST);
        } else if(null == collectionName || CommonConstants.EMPTY_STR.equals(collectionName.trim())) {
            throw new SpotifyException("Collection name can not be null or empty", HttpStatus.BAD_REQUEST);
        }


        List<Collection> collectionList = collectionRepository.fetchByUsernameAndCollectionName(username, collectionName);

        List<CollectionDto> collectionDtoList = new ArrayList<>();

        for(Collection collection: collectionList) {

            CollectionDto collectionDto = collectionToCollectionDto(collection);

            collectionDtoList.add(collectionDto);
        }


        return collectionDtoList;
    }

    @Override
    public UserCollection fetchCollectionDetailsByUsernameAndName(String username, String collectionName) {

        if(null == username || CommonConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be null or empty", HttpStatus.BAD_REQUEST);
        } else if(null == collectionName || CommonConstants.EMPTY_STR.equals(collectionName.trim())) {
            throw new SpotifyException("Collection name can not be null or empty", HttpStatus.BAD_REQUEST);
        }


        UserCollection userCollection = userCollectionRepository.fetchByNameAndUsername(collectionName, username);
        log.info("User Collection Details: {}", userCollection);

        if(null == userCollection) {
            throw new SpotifyException("No collection found with name: " + collectionName + " for user: " + username, HttpStatus.NOT_FOUND);
        }


        return userCollection;
    }

    @Override
    public UserCollection uploadCollectionImageByUsernameAndName(String username, String collectionName, MultipartFile file) {

        if(null == username || CommonConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be null or empty", HttpStatus.BAD_REQUEST);
        } else if(null == collectionName || CommonConstants.EMPTY_STR.equals(collectionName.trim())) {
            throw new SpotifyException("Collection name can not be null or empty", HttpStatus.BAD_REQUEST);
        }


        UserCollection userCollection = userCollectionRepository.fetchByNameAndUsername(collectionName, username);

        if(null == userCollection) {
            throw new SpotifyException("No collection found with name: " + collectionName + " for user: " + username, HttpStatus.NOT_FOUND);
        }


        String url = String.format(cloudinaryConfig.getImageUrl(), cloudinaryConfig.getCloudName());

        String eager = "w_400,h_300,c_pad|w_260,h_200,c_crop";
        String public_id = UUID.randomUUID().toString();

        long instant = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().getEpochSecond();
        String timestamp = String.valueOf(instant);

        String signatureString = "eager=" + eager + "&public_id=" + public_id + "&timestamp=" + timestamp + cloudinaryConfig.getSecret();


        String signature;

        try {
            signature = commonUtil.generateSHAHexValue(signatureString);
            log.info("Signature: {}", signature);
        } catch (SpotifyException e) {
            throw new SpotifyException("SHA algorithm is not valid", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        String collectionImageUrl;

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


            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            log.info("Response Entity: {}", responseEntity);

            if(HttpStatus.OK != responseEntity.getStatusCode()) {
                throw new SpotifyException("Collection image upload failed with status code " + responseEntity.getStatusCode() +
                        " with message: " + responseEntity.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String responseBody = responseEntity.getBody();

            if(null == responseBody) {
                throw new SpotifyException("Response body is null", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("JSON Object: {}", responseBody);

            JSONObject responseJsonObject = new JSONObject(responseBody);
            collectionImageUrl = responseJsonObject.getString("secure_url");

        } catch(IOException e) {
            throw new SpotifyException("Unable to locate file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(RestClientException e) {
            throw new SpotifyException("Unable to upload collection image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        userCollection.setImageUrl(collectionImageUrl);
        userCollection.setLastUpdatedDate(LocalDateTime.now());


        userCollectionRepository.save(userCollection);


        return userCollection;
    }


    private CollectionDto collectionToCollectionDto(Collection collection) {

        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setAlbum(collection.getAlbum());
        collectionDto.setArtists(collection.getArtists());
        collectionDto.setDuration(collection.getDuration());
        collectionDto.setImageUrl(collection.getImageUrl());
        collectionDto.setName(collection.getName());
        collectionDto.setPopularity(collection.getPopularity());
        collectionDto.setReleaseDate(collection.getReleaseDate());
        collectionDto.setSpotifyId(collection.getSpotifyId());
        collectionDto.setTitle(collection.getTitle());
        collectionDto.setTotalTracks(collection.getTotalTracks());
        collectionDto.setType(collection.getType());
        collectionDto.setUsername(collection.getUsername());

        return collectionDto;
    }


}
