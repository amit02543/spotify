package com.amit.spotify.service;

import com.amit.spotify.dto.UserDto;

public interface UserService {

    UserDto fetchUserByUsername(String username);

    String deleteUserByUsername(String username);

}
