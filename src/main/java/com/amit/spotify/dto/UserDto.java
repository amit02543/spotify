package com.amit.spotify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private String name;
    private String pronoun;
    private String email;
    private String profileUrl;

}
