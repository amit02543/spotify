package com.amit.spotify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {

    private List<Album> albums = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();
    private List<Track> tracks = new ArrayList<>();

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    public void addArtist(Artist artist) {
        this.artists.add(artist);
    }

    public void addTrack(Track track) {
        this.tracks.add(track);
    }

}
