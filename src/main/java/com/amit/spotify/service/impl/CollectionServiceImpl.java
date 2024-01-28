package com.amit.spotify.service.impl;

import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.dto.CollectionDto;
import com.amit.spotify.entity.Collection;
import com.amit.spotify.entity.UserCollection;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.repository.CollectionRepository;
import com.amit.spotify.repository.UserCollectionRepository;
import com.amit.spotify.service.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CollectionServiceImpl implements CollectionService {


    @Autowired
    private CollectionRepository collectionRepository;


    @Autowired
    private UserCollectionRepository userCollectionRepository;


    @Override
    public String saveCollection(CollectionDto collectionDto) {

        Collection collection = collectionDtoToCollection(collectionDto);


        collectionRepository.save(collection);

        UserCollection userCollection = userCollectionRepository.fetchByNameAndUsername(collectionDto.getName(), collectionDto.getUsername());
        log.info("Existing user collection {}", userCollection);


        userCollection.setLastUpdatedDate(LocalDateTime.now());

        userCollectionRepository.save(userCollection);

        return collectionDto.getType() + " added successfully to " + collectionDto.getName() + " collection";
    }


    @Override
    public List<Collection> fetchAllCollectionsByUsername(String username) {

        if(null == username || SpotifyConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be null or empty", HttpStatus.BAD_REQUEST);
        }


        return collectionRepository.findAllCollectionsByUsername(username);
    }


    private Collection collectionDtoToCollection(CollectionDto collectionDto) {

        Collection collection = new Collection();
        collection.setAlbum(collectionDto.getAlbum());
        collection.setArtists(collectionDto.getArtists());
        collection.setDuration(collectionDto.getDuration());
        collection.setImageUrl(collectionDto.getImageUrl());
        collection.setName(collectionDto.getName());
        collection.setPopularity(collectionDto.getPopularity());
        collection.setReleaseDate(collectionDto.getReleaseDate());
        collection.setSpotifyId(collectionDto.getId());
        collection.setTitle(collectionDto.getTitle());
        collection.setTotalTracks(collectionDto.getTotalTracks());
        collection.setType(collectionDto.getType());
        collection.setUsername(collectionDto.getUsername());


        return collection;
    }


}
