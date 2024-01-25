package com.amit.spotify.service;

import com.amit.spotify.dto.CollectionDto;
import com.amit.spotify.entity.Collection;

import java.util.List;

public interface CollectionService {

    String saveCollection(CollectionDto collectionDto);

    List<Collection> fetchAllCollectionsByUsername(String username);

}
