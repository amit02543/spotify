package com.amit.spotify.repository;

import com.amit.spotify.entity.UserSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSongRepository extends JpaRepository<UserSong, String> {

    @Query(value = "SELECT * FROM user_song u WHERE u.username = :username", nativeQuery = true)
    List<UserSong> findAllSongsByUsername(String username);

}
