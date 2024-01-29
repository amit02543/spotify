package com.amit.spotify.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCollection {

    private String name;
    private String username;
    private String imageUrl;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;

}
