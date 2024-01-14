package com.amit.spotify.dto;

import com.amit.spotify.model.Album;
import com.amit.spotify.model.Track;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDto {

    private String username;
    private String name;
    private String type;
    private String spotifyId;
    private String title;
    private String album;
    private List<String> artists = new ArrayList<>();
    private String releaseDate;
    private String imageUrl;
    private int totalTracks;
    private String duration;
    private int popularity;

}
