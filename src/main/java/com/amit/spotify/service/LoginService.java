package com.amit.spotify.service;

import com.amit.spotify.dto.LoginDto;
import com.amit.spotify.entity.User;
import com.amit.spotify.exception.SpotifyException;

public interface LoginService {

    User validateUserLogin(LoginDto loginDto) throws SpotifyException;

}
