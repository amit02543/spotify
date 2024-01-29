package com.amit.spotify.service.impl;

import com.amit.spotify.constants.SpotifyMessageConstants;
import com.amit.spotify.entity.UserAlbum;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.Album;
import com.amit.spotify.repository.UserAlbumRepository;
import com.amit.spotify.service.AlbumService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements AlbumService {


    @Autowired
    private UserAlbumRepository userAlbumRepository;


    @Override
    public List<Album> fetchLikedAlbumsByUsername(String username) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }

        return convertUserAlbumListToAlbumList(userAlbumRepository.findAllAlbumsByUsername(username));
    }


    @Override
    @Transactional
    public Album addLikedAlbumToUser(String username, Album album) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(album.getId())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.ALBUM_ID_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(album.getName())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.ALBUM_NAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        UserAlbum userAlbum = new UserAlbum();
        userAlbum.setUsername(username);
        userAlbum.setAlbumId(album.getId());
        userAlbum.setName(album.getName());
        userAlbum.setArtists(album.getArtists());
        userAlbum.setImageUrl(album.getImageUrl());
        userAlbum.setReleaseDate(album.getReleaseDate());
        userAlbum.setTotalTracks(album.getTotalTracks());
        userAlbum.setLikedDate(LocalDateTime.now());


        UserAlbum savedUserAlbum = userAlbumRepository.save(userAlbum);


        return convertUserAlbumToAlbum(savedUserAlbum);
    }


    @Override
    public Album fetchLikedAlbumByUsernameAndId(String username, String albumId) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(albumId)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.ALBUM_ID_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Optional<UserAlbum> optionalUserAlbum = userAlbumRepository.findAlbumByUsernameAndAlbumId(username, albumId);

        if(optionalUserAlbum.isEmpty()) {
            throw new SpotifyException("Selected album not found for user: " + username, HttpStatus.NOT_FOUND);
        }


        return convertUserAlbumToAlbum(optionalUserAlbum.get());
    }


    @Override
    @Transactional
    public String deleteLikedAlbumByUsernameAndId(String username, String albumId) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Optional<UserAlbum> optionalUserAlbum = userAlbumRepository.findAlbumByUsernameAndAlbumId(username, albumId);

        if(optionalUserAlbum.isEmpty()) {
            throw new SpotifyException("Selected album not found for user: " + username, HttpStatus.NOT_FOUND);
        }


        UserAlbum userAlbum = optionalUserAlbum.get();


        userAlbumRepository.deleteById(userAlbum.getId());


        return "Album deleted successfully";
    }


    private List<Album> convertUserAlbumListToAlbumList(List<UserAlbum> userAlbumList) {

        return userAlbumList.stream()
                .map(this::convertUserAlbumToAlbum)
                .collect(Collectors.toList());
    }


    private Album convertUserAlbumToAlbum(UserAlbum userAlbum) {

        Album album = new Album();
        album.setId(userAlbum.getAlbumId());
        album.setName(userAlbum.getName());
        album.setArtists(userAlbum.getArtists());
        album.setReleaseDate(userAlbum.getReleaseDate());
        album.setImageUrl(userAlbum.getImageUrl());
        album.setTotalTracks(userAlbum.getTotalTracks());


        return album;
    }


}
