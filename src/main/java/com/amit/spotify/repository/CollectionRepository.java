package com.amit.spotify.repository;

import com.amit.spotify.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, String> {

    @Query(value = "SELECT * FROM collection c WHERE c.username = :username and c.name = :collectionName", nativeQuery = true)
    List<Collection> fetchByUsernameAndCollectionName(String username, String collectionName);

}
