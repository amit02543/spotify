package com.amit.spotify.service.impl;

import com.amit.spotify.dto.CollectionDto;
import com.amit.spotify.entity.Collection;
import com.amit.spotify.entity.UserCollection;
import com.amit.spotify.repository.CollectionRepository;
import com.amit.spotify.repository.UserCollectionRepository;
import com.amit.spotify.service.CollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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


    private Collection collectionDtoToCollection(CollectionDto collectionDto) {

        Collection collection = new Collection();
        collection.setAlbum(collectionDto.getAlbum());
        collection.setArtists(collectionDto.getArtists());
        collection.setDuration(collectionDto.getDuration());
        collection.setImageUrl(collectionDto.getImageUrl());
        collection.setName(collectionDto.getName());
        collection.setPopularity(collectionDto.getPopularity());
        collection.setReleaseDate(collectionDto.getReleaseDate());
        collection.setSpotifyId(collectionDto.getSpotifyId());
        collection.setTitle(collectionDto.getTitle());
        collection.setTotalTracks(collectionDto.getTotalTracks());
        collection.setType(collectionDto.getType());
        collection.setUsername(collectionDto.getUsername());


        return collection;
    }


}
