package com.amit.spotify.service.impl;

import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.constants.SpotifyMessageConstants;
import com.amit.spotify.dto.CollectionDto;
import com.amit.spotify.entity.Collection;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.UserCollection;
import com.amit.spotify.repository.CollectionRepository;
import com.amit.spotify.service.CollectionService;
import com.amit.spotify.service.UserCollectionService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CollectionServiceImpl implements CollectionService {


    @Autowired
    private CollectionRepository collectionRepository;


    @Autowired
    private UserCollectionService userCollectionService;


    @Override
    public List<CollectionDto> fetchAllCollections() {

        return convertCollectionListToCollectionDtoList(collectionRepository.findAll());
    }


    @Override
    @Transactional
    public CollectionDto addNewCollection(CollectionDto collectionDto) {

        if(StringUtils.isBlank(collectionDto.getUsername())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(collectionDto.getName())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.COLLECTION_NAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(SpotifyConstants.ALBUM.equals(collectionDto.getType())
                && StringUtils.isBlank(collectionDto.getAlbum())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.ALBUM_NAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(SpotifyConstants.TRACK.equals(collectionDto.getType())
                && StringUtils.isBlank(collectionDto.getTitle())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.TRACK_TITLE_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Collection collection = collectionDtoToCollection(collectionDto);

        collectionRepository.save(collection);


        UserCollection userCollection = new UserCollection();
        userCollection.setName(collectionDto.getName());


        userCollectionService.updateCollectionByUsernameAndName(
                collectionDto.getUsername(), collectionDto.getName(), userCollection);


        return convertCollectionToCollectionDto(collection);
    }


    @Override
    public List<CollectionDto> fetchAllCollectionsItemListByUsername(String username) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        List<Collection> collectionList = collectionRepository.findAllCollectionsByUsername(username);


        return convertCollectionListToCollectionDtoList(collectionList);
    }


    @Override
    public List<CollectionDto> fetchCollectionItemListByNameAndUsername(String collectionName, String username) {

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


        List<Collection> collectionList = collectionRepository.fetchByUsernameAndCollectionName(username, collectionName);


        return convertCollectionListToCollectionDtoList(collectionList);
    }


    @Override
    @Transactional
    public String deleteCollectionItemListByNameAndUsername(String collectionName, String username) {

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


        List<Collection> collectionList = collectionRepository.fetchByUsernameAndCollectionName(username, collectionName);


        int albumCount = 0;
        int songCount = 0;


        for(Collection collection: collectionList) {

            if(SpotifyConstants.ALBUM.equals(collection.getType())) {
                albumCount += 1;
            } else if(SpotifyConstants.TRACK.equals(collection.getType())) {
                songCount += 1;
            }

            collectionRepository.deleteById(collection.getId());
        }


        return String.format("%s albums and %s songs deleted successfully from %s collection",
                albumCount, songCount, collectionName);
    }


    @Override
    @Transactional
    public String deleteCollectionItemByNameUsernameAndSpotifyId(String collectionName, String username, String id) {

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
        } else if(StringUtils.isBlank(id)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.ALBUM_SONG_ID_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Optional<Collection> optionalCollection =
                collectionRepository.fetchByUsernameCollectionNameAndSpotifyId(username, collectionName, id);


        if(optionalCollection.isEmpty()) {
            throw new SpotifyException(
                    SpotifyMessageConstants.TRACK_ID_NOT_FOUND_MESSAGE,
                    HttpStatus.NOT_FOUND
            );
        }


        Collection collection = optionalCollection.get();

        collectionRepository.deleteById(collection.getId());

        String name = SpotifyConstants.ALBUM.equals(collection.getType()) ? collection.getName() : collection.getTitle();

        return String.format("%s %s deleted successfully", name, collection.getType());
    }


    private List<CollectionDto> convertCollectionListToCollectionDtoList(List<Collection> collectionList) {

        return collectionList.stream()
                .map(this::convertCollectionToCollectionDto)
                .collect(Collectors.toList());
    }


    private CollectionDto convertCollectionToCollectionDto(Collection collection) {

        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setUsername(collection.getUsername());
        collectionDto.setName(collection.getName());
        collectionDto.setType(collection.getType());
        collectionDto.setId(collection.getSpotifyId());
        collectionDto.setTitle(collection.getTitle());
        collectionDto.setAlbum(collection.getAlbum());
        collectionDto.setArtists(collection.getArtists());
        collectionDto.setReleaseDate(collection.getReleaseDate());
        collectionDto.setImageUrl(collection.getImageUrl());
        collectionDto.setTotalTracks(collection.getTotalTracks());
        collectionDto.setDuration(collection.getDuration());
        collectionDto.setPopularity(collection.getPopularity());


        return collectionDto;
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
