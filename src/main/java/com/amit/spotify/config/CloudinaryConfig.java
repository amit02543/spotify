package com.amit.spotify.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties("cloudinary.api")
public class CloudinaryConfig {

    private String imageUrl;
    private String cloudName;
    private String key;
    private String secret;

}
