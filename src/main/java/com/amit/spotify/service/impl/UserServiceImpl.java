package com.amit.spotify.service.impl;

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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private CollectionRepository collectionRepository;


    @Autowired
    private UserCollectionRepository userCollectionRepository;


    @Autowired
    private UserSongRepository userSongRepository;



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
