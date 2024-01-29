package com.amit.spotify.service;

import com.amit.spotify.model.Album;

import java.util.List;

public interface AlbumService {

    List<Album> fetchLikedAlbumsByUsername(String username);

    Album addLikedAlbumToUser(String username, Album album);

    Album fetchLikedAlbumByUsernameAndId(String username, String albumId);

    String deleteLikedAlbumByUsernameAndId(String username, String albumId);

}
