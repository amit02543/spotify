package com.amit.spotify.service.impl;

import com.amit.spotify.constants.SpotifyMessageConstants;
import com.amit.spotify.entity.UserSong;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.Track;
import com.amit.spotify.repository.UserSongRepository;
import com.amit.spotify.service.SongService;
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
public class SongServiceImpl implements SongService {


    @Autowired
    private UserSongRepository userSongRepository;


    @Override
    public List<Track> fetchLikedSongsByUsername(String username) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }

        return convertUserSongListToTrackList(userSongRepository.findAllSongsByUsername(username));
    }


    @Override
    @Transactional
    public Track addLikedSongToUser(String username, Track track) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(track.getId())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.TRACK_ID_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(track.getTitle())) {
            throw new SpotifyException(
                    SpotifyMessageConstants.TRACK_TITLE_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        UserSong song = new UserSong();
        song.setUsername(username);
        song.setTrackId(track.getId());
        song.setTitle(track.getTitle());
        song.setArtists(track.getArtists());
        song.setAlbum(track.getAlbum());
        song.setReleaseDate(track.getReleaseDate());
        song.setDuration(track.getDuration());
        song.setPopularity(track.getPopularity());
        song.setImageUrl(track.getImageUrl());
        song.setLikedDate(LocalDateTime.now());


        return convertUserSongToTrack(userSongRepository.save(song));
    }


    @Override
    public Track fetchLikedSongByUsernameAndId(String username, String songId) {

        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        } else if(StringUtils.isBlank(songId)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.TRACK_ID_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Optional<UserSong> optionalUserSong = userSongRepository.findSongByUsernameAndSongId(username, songId);

        if(optionalUserSong.isEmpty()) {
            throw new SpotifyException("Selected song not found for user: " + username, HttpStatus.NOT_FOUND);
        }


        return convertUserSongToTrack(optionalUserSong.get());
    }


    @Override
    @Transactional
    public String deleteLikedSongByUsernameAndId(String username, String songId) {
        if(StringUtils.isBlank(username)) {
            throw new SpotifyException(
                    SpotifyMessageConstants.USERNAME_NULL_MESSAGE,
                    HttpStatus.BAD_REQUEST
            );
        }


        Optional<UserSong> songOptional = userSongRepository.findSongByUsernameAndSongId(username, songId);

        if(songOptional.isEmpty()) {
            throw new SpotifyException("Selected song not found for user: " + username, HttpStatus.NOT_FOUND);
        }


        UserSong userSong = songOptional.get();


        userSongRepository.deleteById(userSong.getId());


        return "Song deleted successfully";
    }


    private List<Track> convertUserSongListToTrackList(List<UserSong> userSongList) {

       return userSongList.stream()
               .map(this::convertUserSongToTrack)
               .collect(Collectors.toList());
    }


    private Track convertUserSongToTrack(UserSong userSong) {

        Track track = new Track();
        track.setId(userSong.getTrackId());
        track.setTitle(userSong.getTitle());
        track.setArtists(userSong.getArtists());
        track.setAlbum(userSong.getAlbum());
        track.setDuration(userSong.getDuration());
        track.setPopularity(userSong.getPopularity());
        track.setImageUrl(userSong.getImageUrl());
        track.setReleaseDate(userSong.getReleaseDate());


        return track;
    }


}
