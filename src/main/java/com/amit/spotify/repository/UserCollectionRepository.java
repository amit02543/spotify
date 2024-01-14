package com.amit.spotify.repository;

import com.amit.spotify.entity.UserCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCollectionRepository extends JpaRepository<UserCollection, String> {

    @Query(value = "SELECT * FROM user_collection u WHERE u.username = :username", nativeQuery = true)
    List<UserCollection> findAllByUsername(String username);

    @Query(value = "SELECT * FROM user_collection u WHERE u.username = :username and u.name = :name", nativeQuery = true)
    UserCollection fetchByNameAndUsername(String name, String username);

}
