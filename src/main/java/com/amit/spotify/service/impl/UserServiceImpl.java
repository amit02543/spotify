package com.amit.spotify.service.impl;

import com.amit.spotify.constants.CommonConstants;
import com.amit.spotify.dto.UserCollectionDto;
import com.amit.spotify.entity.UserCollection;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.repository.UserCollectionRepository;
import com.amit.spotify.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserCollectionRepository userCollectionRepository;

    @Override
    public List<UserCollection> fetchCollectionsByUsername(String username) {

        if(null == username || CommonConstants.EMPTY_STR.equals(username.trim())) {
            throw new SpotifyException("Username can not be null or empty", HttpStatus.BAD_REQUEST);
        }

        return userCollectionRepository.findAllByUsername(username);
    }

    @Transactional
    @Override
    public List<UserCollection> addCollectionsByUsername(UserCollectionDto userCollectionDto) {

        if(null == userCollectionDto.getUserName() || CommonConstants.EMPTY_STR.equals(userCollectionDto.getUserName().trim())) {
            throw new SpotifyException("Username can not be null or empty", HttpStatus.BAD_REQUEST);
        } else if(null == userCollectionDto.getCollectionName() || CommonConstants.EMPTY_STR.equals(userCollectionDto.getCollectionName().trim())) {
            throw new SpotifyException("Collection name can not be null or empty", HttpStatus.BAD_REQUEST);
        }

        UserCollection userCollection = new UserCollection();
        userCollection.setUserName(userCollectionDto.getUserName());
        userCollection.setName(userCollectionDto.getCollectionName());
        userCollection.setCreatedDate(LocalDateTime.now());
        userCollection.setLastUpdatedDate(LocalDateTime.now());


        userCollectionRepository.save(userCollection);


        return userCollectionRepository.findAllByUsername(userCollectionDto.getUserName());
    }


}
