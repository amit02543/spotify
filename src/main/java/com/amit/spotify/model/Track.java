package com.amit.spotify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Track {

    private String id;
    private String title;
    private List<String> artists = new ArrayList<>();
    private String album;
    private String duration;
    private int popularity;
    private String imageUrl;
    private String releaseDate;

}
