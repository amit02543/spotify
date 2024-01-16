package com.amit.spotify.service;

import com.amit.spotify.dto.CollectionDto;
import com.amit.spotify.dto.UserCollectionDto;
import com.amit.spotify.entity.UserCollection;
import com.amit.spotify.entity.UserSong;
import com.amit.spotify.model.Track;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserCollection> fetchCollectionsByUsername(String username);

    List<UserCollection> addCollectionsByUsername(UserCollectionDto userCollectionDto);

    List<UserSong> fetchUserLikedSongsByUsername(String username);

    String addUserLikedSongsByUsername(String username, Track track);

    List<CollectionDto> fetchCollectionsByUsernameAndName(String username, String collectionName);

    UserCollection fetchCollectionDetailsByUsernameAndName(String username, String collectionName);

    UserCollection uploadCollectionImageByUsernameAndName(String username, String collectionName, MultipartFile file);

}
