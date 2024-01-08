package com.amit.spotify.service;

import com.amit.spotify.dto.UserCollectionDto;
import com.amit.spotify.entity.UserCollection;

import java.util.List;

public interface UserService {

    List<UserCollection> fetchCollectionsByUsername(String username);

    List<UserCollection> addCollectionsByUsername(UserCollectionDto userCollectionDto);

}
