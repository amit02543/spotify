package com.amit.spotify.service.impl;

import com.amit.spotify.config.SpotifyConfig;
import com.amit.spotify.exception.SpotifyException;
import com.amit.spotify.service.SpotifyService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SpotifyServiceImpl implements SpotifyService {


    @Autowired
    private SpotifyConfig spotifyConfig;


    @Autowired
    private RestTemplate restTemplate;


    @Override
    public void searchByTermAndType(String query, String type) {

        String accessToken = spotifyConfig.getAccessToken();
        log.info("Access Token: {}", accessToken);

        String searchUrl = spotifyConfig.getSearchUrl();

        String formattedSearchUrl = String.format(searchUrl, query.toLowerCase(), type.toLowerCase());
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


        if(!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            throw new SpotifyException("Unable to get search result", (HttpStatus) responseEntity.getStatusCode());
        }


        if(null == responseEntity.getBody()) {
            throw new SpotifyException("Search result response is null", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        JSONObject responseJsonObject = new JSONObject(responseEntity.getBody());
        log.info("Response: {}", responseJsonObject);


    }


}
