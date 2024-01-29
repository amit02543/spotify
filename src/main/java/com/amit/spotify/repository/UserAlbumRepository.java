package com.amit.spotify.repository;

import com.amit.spotify.entity.UserAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAlbumRepository extends JpaRepository<UserAlbum, String> {

    @Query(value = "SELECT * FROM user_album u WHERE u.username = :username", nativeQuery = true)
    List<UserAlbum> findAllAlbumsByUsername(String username);

    @Query(value = "SELECT * FROM user_album u WHERE u.username = :username and u.album_id = :albumId", nativeQuery = true)
    Optional<UserAlbum> findAlbumByUsernameAndAlbumId(String username, String albumId);

}
