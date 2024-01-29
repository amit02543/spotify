package com.amit.spotify.service;

import com.amit.spotify.dto.UserCollectionDto;
import com.amit.spotify.model.UserCollection;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserCollectionService {

    List<UserCollection> fetchAllCollectionsByUsername(String username);

    UserCollection addUserCollection(UserCollectionDto userCollectionDto);

    UserCollection fetchCollectionByUsernameAndName(String username, String collectionName);

    UserCollection updateCollectionByUsernameAndName(String username, String collectionName, UserCollection userCollection);

    String deleteCollectionByUsernameAndName(String username, String collectionName);

    UserCollection uploadCollectionImageByUsernameAndName(String username, String collectionName, MultipartFile file);

    String deleteCollectionImageByUsernameAndName(String username, String collectionName);

}
