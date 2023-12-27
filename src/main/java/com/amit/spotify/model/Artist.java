package com.amit.spotify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {

    private String id;
    private String name;
    private List<String> genres = new ArrayList<>();
    private int followers;
    private int popularity;
    private String imageUrl;

}
