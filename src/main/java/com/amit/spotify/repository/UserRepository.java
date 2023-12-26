package com.amit.spotify.repository;

import com.amit.spotify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM user u WHERE u.username = :username", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE user u SET u.name = :name, u.pronoun = :pronoun, u.email = :email WHERE u.username = :username", nativeQuery = true)
    void updateUserByUsername(@Param("username") String username, @Param("name") String name, @Param("pronoun") String pronoun, @Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE user u SET u.profile_url = :profileUrl WHERE u.username = :username", nativeQuery = true)
    void updateUserProfileUrlByUsername(@Param("username") String username, @Param("profileUrl") String profileUrl);


}
