package com.amit.spotify.service;

import com.amit.spotify.model.SearchResult;

public interface SpotifyService {

    SearchResult searchByTermAndType(String query, String type, int offset);

    SearchResult fetchLatestAlbums(String username);

    SearchResult fetchRandomTracks(String username);

}
