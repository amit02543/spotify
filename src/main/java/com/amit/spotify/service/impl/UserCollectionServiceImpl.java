package com.amit.spotify.service.impl;

import com.amit.spotify.constants.SpotifyMessageConstants;
import com.amit.spotify.dto.UserCollectionDto;
import com.amit.spotify.entity.Collection;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.UserCollection;
import com.amit.spotify.repository.CollectionRepository;
import com.amit.spotify.repository.UserCollectionRepository;
import com.amit.spotify.service.UserCollectionService;
import com.amit.spotify.util.SpotifyImageUploadUtility;
import com.amit.spotify.util.SpotifyUtility;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserCollectionServiceImpl implements UserCollectionService {


    @Autowired
    private CollectionRepository collectionRepository;


    @Autowired
    private UserCollectionRepository userCollectionRepository;


    @Autowired
    private SpotifyImageUploadUtility spotifyImageUploadUtility;


    @Autowired
    private SpotifyUtility spotifyUtility;



    @Override
    public List<UserCollection> fetchAllCollectionsByUsername(String username) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        return convertUserCollectionEntityListToUserCollectionList(userCollectionRepository.findAllByUsername(username));
    }


    @Override
    @Transactional
    public UserCollection addUserCollection(UserCollectionDto userCollectionDto) {

        if(StringUtils.isBlank(userCollectionDto.getUserName())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(userCollectionDto.getCollectionName())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.COLLECTION_NAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        com.amit.spotify.entity.UserCollection userCollection = new com.amit.spotify.entity.UserCollection();
        userCollection.setUserName(userCollectionDto.getUserName());
        userCollection.setName(userCollectionDto.getCollectionName());
        userCollection.setCreatedDate(LocalDateTime.now());
        userCollection.setLastUpdatedDate(LocalDateTime.now());


        return convertUserCollectionEntityToUserCollection(userCollectionRepository.save(userCollection));
    }


    @Override
    public UserCollection fetchCollectionByUsernameAndName(String username, String collectionName) {

        com.amit.spotify.entity.UserCollection userCollection = getUserCollection(username, collectionName);


        return convertUserCollectionEntityToUserCollection(userCollection);
    }


    @Override
    @Transactional
    public UserCollection updateCollectionByUsernameAndName(String username, String collectionName, UserCollection userCollection) {

        com.amit.spotify.entity.UserCollection userCollectionEntity = getUserCollection(username, collectionName);

        userCollectionEntity.setName(userCollection.getName());
        userCollectionEntity.setLastUpdatedDate(LocalDateTime.now());


        com.amit.spotify.entity.UserCollection savedCollection = userCollectionRepository.save(userCollectionEntity);


        return convertUserCollectionEntityToUserCollection(savedCollection);
    }


    @Override
    @Transactional
    public String deleteCollectionByUsernameAndName(String username, String collectionName) {

        List<Collection> collectionList =
                collectionRepository.fetchByUsernameAndCollectionName(username, collectionName);

        for(Collection collection: collectionList) {
            collectionRepository.deleteById(collection.getId());
        }

        com.amit.spotify.entity.UserCollection userCollectionEntity = getUserCollection(username, collectionName);

        userCollectionRepository.deleteById(userCollectionEntity.getId());

        return collectionName + " deleted successfully";
    }


    @Override
    @Transactional
    public UserCollection uploadCollectionImageByUsernameAndName(String username, String collectionName, MultipartFile file) {

        com.amit.spotify.entity.UserCollection userCollectionEntity = getUserCollection(username, collectionName);

        String uploadImageUrl = spotifyImageUploadUtility.uploadImage(file);

        userCollectionEntity.setImageUrl(uploadImageUrl);

        com.amit.spotify.entity.UserCollection savedCollection = userCollectionRepository.save(userCollectionEntity);

        return convertUserCollectionEntityToUserCollection(savedCollection);
    }


    @Override
    @Transactional
    public String deleteCollectionImageByUsernameAndName(String username, String collectionName) {

        com.amit.spotify.entity.UserCollection userCollectionEntity = getUserCollection(username, collectionName);

        String url = userCollectionEntity.getImageUrl();

        userCollectionEntity.setImageUrl(null);

        //TODO: Delete image from cloudinary

        userCollectionRepository.save(userCollectionEntity);

        String publicId = spotifyUtility.extractPublicIdFromUrl(url);

        spotifyImageUploadUtility.removeImage(publicId);


        return collectionName + " image is removed successfully";
    }


    private List<UserCollection> convertUserCollectionEntityListToUserCollectionList(List<com.amit.spotify.entity.UserCollection> entityCollectionList) {

        return entityCollectionList.stream()
                .map(this::convertUserCollectionEntityToUserCollection)
                .collect(Collectors.toList());
    }


    private UserCollection convertUserCollectionEntityToUserCollection(com.amit.spotify.entity.UserCollection entityCollection) {

        UserCollection userCollection = new UserCollection();
        userCollection.setName(entityCollection.getName());
        userCollection.setUsername(entityCollection.getUsername());
        userCollection.setImageUrl(entityCollection.getImageUrl());
        userCollection.setCreatedDate(entityCollection.getCreatedDate());
        userCollection.setLastUpdatedDate(entityCollection.getLastUpdatedDate());


        return userCollection;
    }


    private com.amit.spotify.entity.UserCollection getUserCollection(String username, String collectionName) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(collectionName)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.COLLECTION_NAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Optional<com.amit.spotify.entity.UserCollection> optionalUserCollection =
                userCollectionRepository.fetchByNameAndUsername(collectionName, username);

        if(optionalUserCollection.isEmpty()) {
            throw new SpotifyException(
                    String.format(SpotifyMessageConstants.COLLECTION_NOT_FOUND_MESSAGE, collectionName, username),
                    HttpStatus.NOT_FOUND
            );
        }



        return optionalUserCollection.get();
    }


}
