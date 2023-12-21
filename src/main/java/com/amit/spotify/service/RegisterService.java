package com.amit.spotify.service;

import com.amit.spotify.dto.SignUpDto;
import com.amit.spotify.entity.User;

public interface RegisterService {

    User registerUser(SignUpDto signUpDto);

}
