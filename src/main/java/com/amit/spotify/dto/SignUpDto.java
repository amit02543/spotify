package com.amit.spotify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;

}
