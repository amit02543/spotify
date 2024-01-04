package com.amit.spotify.service.impl;

import com.amit.spotify.config.SpotifyConfig;
import com.amit.spotify.constants.CommonConstants;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.model.SearchResult;
import com.amit.spotify.service.SpotifyService;
import com.amit.spotify.util.SearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SpotifyServiceImpl implements SpotifyService {


    @Autowired
    private SpotifyConfig spotifyConfig;


    @Autowired
    private SearchUtil searchUtil;


    @Autowired
    private RestTemplate restTemplate;


    @Override
    public SearchResult searchByTermAndType(String query, String type) {

        if(null == query) {
            throw new SpotifyException("Search query cannot be null", HttpStatus.BAD_REQUEST);
        } else if(CommonConstants.EMPTY_STR.equals(query.trim())) {
            throw new SpotifyException("Search query cannot be empty", HttpStatus.BAD_REQUEST);
        } else if(null == type) {
            throw new SpotifyException("Search type cannot be null", HttpStatus.BAD_REQUEST);
        } else if(CommonConstants.EMPTY_STR.equals(type.trim())) {
            throw new SpotifyException("Search type cannot be empty", HttpStatus.BAD_REQUEST);
        } else if(!CommonConstants.TYPE_LIST.contains(type.toLowerCase().trim())) {
            throw new SpotifyException("Not a valid search type", HttpStatus.BAD_REQUEST);
        }


        String accessToken = spotifyConfig.getAccessToken();
        log.info("Access Token: {}", accessToken);


        String searchUrl = spotifyConfig.getSearchUrl();

        String formattedSearchUrl = String.format(searchUrl, query.toLowerCase().trim(), type.toLowerCase().trim());
        log.info("Formatted URL: {}", formattedSearchUrl);


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> responseEntity;

        try {
            responseEntity = restTemplate.exchange(formattedSearchUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            log.info("Search Response Entity: {}", responseEntity);
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

        if(CommonConstants.ALBUM.equals(typeLC)) {
            itemsArray = resultJsonObject.getJSONObject("albums").getJSONArray(CommonConstants.ITEMS);
        } else if(CommonConstants.ARTIST.equals(typeLC)) {
            itemsArray = resultJsonObject.getJSONObject("artists").getJSONArray(CommonConstants.ITEMS);
        } else if(CommonConstants.TRACK.equals(typeLC)) {
            itemsArray = resultJsonObject.getJSONObject("tracks").getJSONArray(CommonConstants.ITEMS);
        }

        return searchUtil.formatSearchResult(itemsArray, typeLC);
    }


    @Override
    public SearchResult fetchLatestAlbums() {

        String accessToken = spotifyConfig.getAccessToken();
        log.info("Access Token: {}", accessToken);

        String formattedNewReleaseUrl = String.format(spotifyConfig.getBaseUrl() + "browse/new-releases");
        log.info("Formatted URL: {}", formattedNewReleaseUrl);


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> responseEntity;

        try {
            responseEntity = restTemplate.exchange(formattedNewReleaseUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            log.info("Formatted New Release Response Entity: {}", responseEntity);
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
        JSONArray itemsArray = resultJsonObject.getJSONObject("albums").getJSONArray(CommonConstants.ITEMS);


        return searchUtil.formatSearchResult(itemsArray, CommonConstants.ALBUM);
    }


    @Override
    public SearchResult fetchRandomTracks() {

        String query = getRandomSearchTerm();

        String type = CommonConstants.TRACK;


        return searchByTermAndType(query, type);
    }


    private String getRandomSearchTerm() {

        String characters = "abcdefghijklmnopqrstuvwxyz";

        String randomCharacter = Character.toString(characters.charAt((int) Math.floor(Math.random() * characters.length())));


        return switch ((int) Math.round(Math.random())) {
            case 0 -> randomCharacter + '%';
            case 1 -> '%' + randomCharacter + '%';
            default -> CommonConstants.EMPTY_STR;
        };

    }


}
