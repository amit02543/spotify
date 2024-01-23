package com.amit.spotify.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user_album", uniqueConstraints = {
        @UniqueConstraint(name = "idx_username_trackId", columnNames = {"username", "albumId"})
})
public class UserAlbum implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "album_id", nullable = false)
    private String albumId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "album_name", nullable = false)
    private String name;

    @Column(name = "album_artists", nullable = false)
    private List<String> artists = new ArrayList<>();

    @Column(name = "album_tracks_count", nullable = false)
    private int totalTracks;

    @Column(name = "album_image_url", nullable = false)
    private String imageUrl;

    @Column(name = "album_release_date", nullable = false)
    private String releaseDate;

    @Column(name = "liked_date", nullable = false)
    @CreatedDate
    private LocalDateTime likedDate;


    public UserAlbum() { }


    public UserAlbum(String id, String albumId, String username, String name, List<String> artists, int totalTracks, String imageUrl, String releaseDate, LocalDateTime likedDate) {
        this.id = id;
        this.albumId = albumId;
        this.username = username;
        this.name = name;
        this.artists = artists;
        this.totalTracks = totalTracks;
        this.imageUrl = imageUrl;
        this.releaseDate = releaseDate;
        this.likedDate = likedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
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

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDateTime getLikedDate() {
        return likedDate;
    }

    public void setLikedDate(LocalDateTime likedDate) {
        this.likedDate = likedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAlbum userAlbum = (UserAlbum) o;
        return totalTracks == userAlbum.totalTracks && Objects.equals(id, userAlbum.id) && Objects.equals(albumId, userAlbum.albumId) && Objects.equals(username, userAlbum.username) && Objects.equals(name, userAlbum.name) && Objects.equals(artists, userAlbum.artists) && Objects.equals(imageUrl, userAlbum.imageUrl) && Objects.equals(releaseDate, userAlbum.releaseDate) && Objects.equals(likedDate, userAlbum.likedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, albumId, username, name, artists, totalTracks, imageUrl, releaseDate, likedDate);
    }

    @Override
    public String toString() {
        return "UserAlbum{" +
                "id='" + id + '\'' +
                ", albumId='" + albumId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", artists=" + artists +
                ", totalTracks=" + totalTracks +
                ", imageUrl='" + imageUrl + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", likedDate=" + likedDate +
                '}';
    }

}
