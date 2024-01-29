package com.amit.spotify.service;

import com.amit.spotify.model.Track;

import java.util.List;

public interface SongService {

    List<Track> fetchLikedSongsByUsername(String username);

    Track addLikedSongToUser(String username, Track track);

    Track fetchLikedSongByUsernameAndId(String username, String songId);

    String deleteLikedSongByUsernameAndId(String username, String songId);

}
