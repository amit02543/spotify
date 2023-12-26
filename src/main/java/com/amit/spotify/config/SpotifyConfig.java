package com.amit.spotify.config;

import com.amit.spotify.exception.SpotifyException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties("spotify.api")
@Slf4j
public class SpotifyConfig {

    private String clientId;
    private String clientSecret;
    private String tokenUrl;
    private String searchUrl;
    private String artistUrl;


    @Autowired
    private RestTemplate restTemplate;


    public String getAccessToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity;

        try {
            responseEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);
        } catch(RestClientException e) {
            log.error("Exception occurred while calling token URL: {}", e.getMessage());
            throw new SpotifyException("Exception occurred while calling token URL", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        if(!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            throw new SpotifyException("Unable to get access token", (HttpStatus) responseEntity.getStatusCode());
        }


        if(null == responseEntity.getBody()) {
            throw new SpotifyException("Token response is null", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        JSONObject responseJsonObject = new JSONObject(responseEntity.getBody());


        return responseJsonObject.getString("access_token");
    }


}
