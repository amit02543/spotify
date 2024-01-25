package com.amit.spotify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {

    private String id;
    private String name;
    private List<String> artists = new ArrayList<>();
    private String releaseDate;
    private String imageUrl;
    private int totalTracks;
    private boolean isLiked;
    private String collection;

}
