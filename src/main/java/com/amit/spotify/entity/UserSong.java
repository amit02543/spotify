package com.amit.spotify.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user_song", uniqueConstraints = {
        @UniqueConstraint(name = "idx_username_trackId", columnNames = {"username", "trackId"})
})
public class UserSong implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "track_id", nullable = false)
    private String trackId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "track_title", nullable = false)
    private String title;

    @Column(name = "track_artists", nullable = false)
    private List<String> artists = new ArrayList<>();

    @Column(name = "track_album", nullable = false)
    private String album;

    @Column(name = "track_duration", nullable = false)
    private String duration;

    @Column(name = "track_image_url", nullable = false)
    private String imageUrl;

    @Column(name = "track_release_date", nullable = false)
    private String releaseDate;

    @Column(name = "liked_date", nullable = false)
    @CreatedDate
    private LocalDateTime likedDate;


    public UserSong() { }


    public UserSong(String id, String trackId, String username, String title, List<String> artists, String album,
                    String duration, String imageUrl, String releaseDate, LocalDateTime likedDate) {
        this.id = id;
        this.trackId = trackId;
        this.username = username;
        this.title = title;
        this.artists = artists;
        this.album = album;
        this.duration = duration;
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

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String trackTitle) {
        this.title = trackTitle;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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
        UserSong userSong = (UserSong) o;
        return Objects.equals(id, userSong.id) && Objects.equals(trackId, userSong.trackId) && Objects.equals(username, userSong.username) && Objects.equals(title, userSong.title) && Objects.equals(artists, userSong.artists) && Objects.equals(album, userSong.album) && Objects.equals(duration, userSong.duration) && Objects.equals(imageUrl, userSong.imageUrl) && Objects.equals(releaseDate, userSong.releaseDate) && Objects.equals(likedDate, userSong.likedDate);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, trackId, username, title, artists, album, duration, imageUrl, releaseDate, likedDate);
    }


    @Override
    public String toString() {
        return "UserSong{" +
                "id='" + id + '\'' +
                ", trackId='" + trackId + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", artists=" + artists +
                ", album='" + album + '\'' +
                ", duration='" + duration + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", likedDate=" + likedDate +
                '}';
    }


}
