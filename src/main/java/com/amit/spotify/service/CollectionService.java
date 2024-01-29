package com.amit.spotify.service;

import com.amit.spotify.dto.CollectionDto;
import com.amit.spotify.entity.Collection;

import java.util.List;

public interface CollectionService {

    List<CollectionDto> fetchAllCollections();

    CollectionDto addNewCollection(CollectionDto collectionDto);

    List<CollectionDto> fetchAllCollectionsItemListByUsername(String username);

    List<CollectionDto> fetchCollectionItemListByNameAndUsername(String collectionName, String username);

    String deleteCollectionItemListByNameAndUsername(String collectionName, String username);

    String deleteCollectionItemByNameUsernameAndSpotifyId(String collectionName, String username, String id);

}
