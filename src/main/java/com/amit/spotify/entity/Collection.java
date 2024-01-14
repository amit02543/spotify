package com.amit.spotify.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "collection", uniqueConstraints = {
        @UniqueConstraint(name = "idx_album_name_title_username", columnNames = {"album", "name", "title", "username"})
})
public class Collection implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "spotifyId")
    private String spotifyId;

    @Column(name = "title", nullable = false, length = 80)
    private String title;

    @Column(name = "album", nullable = false, length = 80)
    private String album;

    @Column(name = "artists")
    private List<String> artists = new ArrayList<>();

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "total_tracks")
    private int totalTracks;

    @Column(name = "duration")
    private String duration;

    @Column(name = "popularity")
    private int popularity;


    public Collection() { }


    public Collection(String id, String username, String name, String type, String spotifyId, String title,
                      String album, List<String> artists, String releaseDate, String imageUrl,
                      int totalTracks, String duration, int popularity) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.type = type;
        this.spotifyId = spotifyId;
        this.title = title;
        this.album = album;
        this.artists = artists;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
        this.totalTracks = totalTracks;
        this.duration = duration;
        this.popularity = popularity;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return totalTracks == that.totalTracks && popularity == that.popularity && Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(spotifyId, that.spotifyId) && Objects.equals(title, that.title) && Objects.equals(album, that.album) && Objects.equals(artists, that.artists) && Objects.equals(releaseDate, that.releaseDate) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(duration, that.duration);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, username, name, type, spotifyId, title, album, artists, releaseDate, imageUrl, totalTracks, duration, popularity);
    }


    @Override
    public String toString() {
        return "Collection{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", spotifyId='" + spotifyId + '\'' +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", artists=" + artists +
                ", releaseDate='" + releaseDate + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", totalTracks=" + totalTracks +
                ", duration='" + duration + '\'' +
                ", popularity=" + popularity +
                '}';
    }



}
