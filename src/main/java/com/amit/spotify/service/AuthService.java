package com.amit.spotify.service;

import com.amit.spotify.dto.LoginDto;
import com.amit.spotify.dto.SignUpDto;
import com.amit.spotify.entity.User;

public interface AuthService {

    User registerUser(SignUpDto signUpDto);


    User validateUserLogin(LoginDto loginDto);

}
