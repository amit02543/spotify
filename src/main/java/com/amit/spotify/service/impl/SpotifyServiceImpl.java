package com.amit.spotify.service.impl;

import com.amit.spotify.config.SpotifyConfig;
import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.entity.Collection;
import com.amit.spotify.entity.UserAlbum;
import com.amit.spotify.entity.UserSong;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.SearchResult;
import com.amit.spotify.service.CollectionService;
import com.amit.spotify.service.SpotifyService;
import com.amit.spotify.service.UserService;
import com.amit.spotify.util.SearchUtil;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SpotifyServiceImpl implements SpotifyService {


    @Autowired
    private SpotifyConfig spotifyConfig;


    @Autowired
    private SearchUtil searchUtil;


    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private CollectionService collectionService;


    @Autowired
    private UserService userService;


    @Override
    public SearchResult searchByTermAndType(String query, String type, int offset) {

        if(StringUtils.isBlank(query)) {
            throw new SpotifyException("Search query cannot be null or empty", HttpStatus.BAD_REQUEST);
        } else if(StringUtils.isBlank(type)) {
            throw new SpotifyException("Search type cannot be null or empty", HttpStatus.BAD_REQUEST);
        } else if(!SpotifyConstants.TYPE_LIST.contains(type.toLowerCase().trim())) {
            throw new SpotifyException("Not a valid search type", HttpStatus.BAD_REQUEST);
        }


        String accessToken = spotifyConfig.getAccessToken();
        log.info("Access Token: {}", accessToken);


        String searchUrl = spotifyConfig.getSearchUrl();

        String formattedSearchUrl = String.format(searchUrl, query.toLowerCase().trim(), type.toLowerCase().trim(), offset);
        log.info("Formatted URL: {}", formattedSearchUrl);


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> responseEntity;

        try {
            responseEntity = restTemplate.exchange(formattedSearchUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch(RestClientException e) {
            log.error("Exception occurred while calling search URL: {}", e.getMessage());
            throw new SpotifyException("Exception occurred while calling search URL", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        if(HttpStatus.UNAUTHORIZED.equals(responseEntity.getStatusCode())) {
            throw new SpotifyException("Not authorized for search query", HttpStatus.UNAUTHORIZED);
        } else if(HttpStatus.FORBIDDEN.equals(responseEntity.getStatusCode())) {
            throw new SpotifyException("Bad OAuth request for search query", HttpStatus.FORBIDDEN);
        } else if(HttpStatus.TOO_MANY_REQUESTS.equals(responseEntity.getStatusCode())) {
            throw new SpotifyException("User has exceeded rate limits", HttpStatus.TOO_MANY_REQUESTS);
        }


        if(null == responseEntity.getBody()) {
            throw new SpotifyException("Search result response is null", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        JSONObject resultJsonObject = new JSONObject(responseEntity.getBody());
        JSONArray itemsArray = new JSONArray();

        String typeLC = type.toLowerCase().trim();

        if(SpotifyConstants.ALBUM.equals(typeLC)) {
            itemsArray = resultJsonObject.getJSONObject("albums").getJSONArray(SpotifyConstants.ITEMS);
        } else if(SpotifyConstants.ARTIST.equals(typeLC)) {
            itemsArray = resultJsonObject.getJSONObject("artists").getJSONArray(SpotifyConstants.ITEMS);
        } else if(SpotifyConstants.TRACK.equals(typeLC)) {
            itemsArray = resultJsonObject.getJSONObject("tracks").getJSONArray(SpotifyConstants.ITEMS);
        }

        return searchUtil.formatSearchResult(itemsArray, typeLC);
    }


    @Override
    public SearchResult fetchLatestAlbums(String username) {

        String accessToken = spotifyConfig.getAccessToken();
        log.info("Access Token: {}", accessToken);

        String formattedNewReleaseUrl = String.format(spotifyConfig.getBaseUrl() + "browse/new-releases");
        log.info("Formatted URL: {}", formattedNewReleaseUrl);


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> responseEntity;

        try {
            responseEntity = restTemplate.exchange(formattedNewReleaseUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch(RestClientException e) {
            log.error("Exception occurred while calling search URL: {}", e.getMessage());
            throw new SpotifyException("Exception occurred while calling search URL", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        if(HttpStatus.UNAUTHORIZED.equals(responseEntity.getStatusCode())) {
            throw new SpotifyException("Not authorized for search query", HttpStatus.UNAUTHORIZED);
        } else if(HttpStatus.FORBIDDEN.equals(responseEntity.getStatusCode())) {
            throw new SpotifyException("Bad OAuth request for search query", HttpStatus.FORBIDDEN);
        } else if(HttpStatus.TOO_MANY_REQUESTS.equals(responseEntity.getStatusCode())) {
            throw new SpotifyException("User has exceeded rate limits", HttpStatus.TOO_MANY_REQUESTS);
        }


        if(null == responseEntity.getBody()) {
            throw new SpotifyException("Search result response is null", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        JSONObject resultJsonObject = new JSONObject(responseEntity.getBody());
        JSONArray itemsArray = resultJsonObject.getJSONObject("albums").getJSONArray(SpotifyConstants.ITEMS);


        SearchResult searchResult = searchUtil.formatSearchResult(itemsArray, SpotifyConstants.ALBUM);

        if(StringUtils.isNotBlank(username)) {

            List<UserAlbum> userAlbums = userService.fetchLikedAlbumsByUsername(username);

            List<String> userLikedAlbumIds = userAlbums.stream().map(UserAlbum::getAlbumId).toList();


            List<Collection> collectionList = collectionService.fetchAllCollectionsByUsername(username);

            Map<String, String> collectionMap = collectionList.stream()
                    .collect(Collectors.toMap(
                            Collection::getSpotifyId,
                            Collection::getName,
                            (oldValue, newValue) -> newValue));


            searchResult.getAlbums().forEach(album -> {

                if(userLikedAlbumIds.contains(album.getId())) {
                    album.setLiked(true);
                }

                if(collectionMap.containsKey(album.getId())) {
                    album.setCollection(collectionMap.get(album.getId()));
                }

            });

        }


        return searchResult;
    }


    @Override
    public SearchResult fetchRandomTracks(String username) {

        SearchResult searchResult;
        int resultCount;

        do {

            String query = getRandomSearchTerm();

            String type = SpotifyConstants.TRACK;

            int offset = new Random().ints(0, 500).findFirst().getAsInt();

            searchResult = searchByTermAndType(query, type, offset);

            resultCount = searchResult.getAlbums().size() + searchResult.getArtists().size() + searchResult.getTracks().size();

        } while(resultCount < 20);


        if(StringUtils.isNotBlank(username)) {

            List<UserSong> userSongs = userService.fetchLikedSongsByUsername(username);

            List<String> userLikedSongIds = userSongs.stream().map(UserSong::getTrackId).toList();


            List<UserAlbum> userAlbums = userService.fetchLikedAlbumsByUsername(username);

            List<String> userLikedAlbumIds = userAlbums.stream().map(UserAlbum::getAlbumId).toList();


            List<Collection> collectionList = collectionService.fetchAllCollectionsByUsername(username);

            Map<String, String> collectionMap = collectionList.stream()
                    .collect(Collectors.toMap(
                            Collection::getSpotifyId,
                            Collection::getName,
                            (oldValue, newValue) -> newValue));


            searchResult.getTracks().forEach(track -> {

                if(userLikedSongIds.contains(track.getId())) {
                    track.setLiked(true);
                }

                if(collectionMap.containsKey(track.getId())) {
                    track.setCollection(collectionMap.get(track.getId()));
                }

            });


            searchResult.getAlbums().forEach(album -> {

                if(userLikedAlbumIds.contains(album.getId())) {
                    album.setLiked(true);
                }

                if(collectionMap.containsKey(album.getId())) {
                    album.setCollection(collectionMap.get(album.getId()));
                }

            });

        }


        return searchResult;
    }


    private String getRandomSearchTerm() {

        String characters = "abcdefghijklmnopqrstuvwxyz";

        String randomCharacter = Character.toString(characters.charAt((int) Math.floor(Math.random() * characters.length())));


        return switch ((int) Math.round(Math.random())) {
            case 0 -> randomCharacter + '%';
            case 1 -> '%' + randomCharacter + '%';
            default -> SpotifyConstants.EMPTY_STR;
        };

    }


}
