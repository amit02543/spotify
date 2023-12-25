package com.amit.spotify.service;

import com.amit.spotify.dto.UserDto;
import com.amit.spotify.model.Profile;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    UserDto fetchProfileByUsername(String username);

    UserDto updateProfileByUsername(String username, Profile profile);

    UserDto uploadProfileImageByUsername(String username, MultipartFile file);

}
