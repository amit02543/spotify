package com.amit.spotify.util;

import com.amit.spotify.config.CloudinaryConfig;
import com.amit.spotify.constants.SpotifyConstants;
import com.amit.spotify.constants.SpotifyMessageConstants;
import com.amit.spotify.exception.SpotifyException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class SpotifyImageUploadUtility {


    @Autowired
    private CloudinaryConfig cloudinaryConfig;


    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private SpotifyUtility spotifyUtility;


    public String uploadImage(MultipartFile file) {

        String url = String.format(cloudinaryConfig.getImageUrl(), cloudinaryConfig.getCloudName());


        String eager = SpotifyConstants.IMAGE_DIMENSIONS;
        String public_id = UUID.randomUUID().toString();


        long instant = LocalDateTime.now().atZone(ZoneId.of(SpotifyConstants.UTC)).toInstant().getEpochSecond();
        String timestamp = String.valueOf(instant);


        String signatureString = String.format(SpotifyConstants.IMAGE_SIGNATURE, eager, public_id, timestamp, cloudinaryConfig.getSecret());


        String signature;

        try {
            signature = spotifyUtility.generateSHAHexValue(signatureString);
        } catch (SpotifyException e) {
            throw new SpotifyException(
                    SpotifyMessageConstants.SHA_NOT_VALID_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }


        String imageUrl;

        try {

            byte[] fileContent = file.getBytes();
            String filename = file.getName();

            ContentDisposition contentDisposition = ContentDisposition
                    .builder(SpotifyConstants.FORM_DATA)
                    .name(SpotifyConstants.FILE)
                    .filename(filename)
                    .build();


            MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
            fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());


            HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileContent, fileMap);

            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add(SpotifyConstants.FILE, fileEntity);
            requestBody.put(SpotifyConstants.API_KEY, List.of(cloudinaryConfig.getKey()));
            requestBody.put(SpotifyConstants.EAGER, List.of(eager));
            requestBody.put(SpotifyConstants.PUBLIC_ID, List.of(public_id));
            requestBody.put(SpotifyConstants.TIMESTAMP, List.of(timestamp));
            requestBody.put(SpotifyConstants.SIGNATURE, List.of(signature));


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);


            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);


            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if(HttpStatus.OK != responseEntity.getStatusCode()) {
                throw new SpotifyException(
                        String.format(SpotifyMessageConstants.IMAGE_UPLOAD_FAILED_MESSAGE, responseEntity.getStatusCode(),
                                responseEntity.getBody()),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }


            String responseBody = responseEntity.getBody();

            if(null == responseBody) {
                throw new SpotifyException(
                        SpotifyMessageConstants.RESPONSE_BODY_NULL_MESSAGE,
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }


            JSONObject responseJsonObject = new JSONObject(responseBody);
            imageUrl = responseJsonObject.getString(SpotifyConstants.SECURE_URL);


        } catch(IOException e) {
            throw new SpotifyException(
                    String.format(SpotifyMessageConstants.FILE_NOT_FOUND_ERROR_MESSAGE, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch(RestClientException e) {
            throw new SpotifyException(
                    String.format(SpotifyMessageConstants.IMAGE_UPLOAD_ERROR_MESSAGE, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }


        return imageUrl;
    }


}
