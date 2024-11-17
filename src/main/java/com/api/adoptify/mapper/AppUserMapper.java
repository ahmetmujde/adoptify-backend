package com.api.adoptify.mapper;

import com.api.adoptify.dto.request.AppUserRequestDto;
import com.api.adoptify.dto.response.AppUserResponseDto;
import com.api.adoptify.entity.AppUser;

public class AppUserMapper {

    public static AppUser mapToAppUser(AppUserRequestDto appUserRequestDto) {
        if (appUserRequestDto == null) {
            return null;
        }

        AppUser appUser = new AppUser();
        appUser.setUserName(appUserRequestDto.getUserName());
        appUser.setEmail(appUserRequestDto.getEmail());
        appUser.setPassword(appUserRequestDto.getPassword());
        return appUser;
    }

    public static AppUserResponseDto mapToAppUserResponseDto(AppUser appUser) {
        if (appUser == null) {
            return null;
        }

        AppUserResponseDto responseDto = new AppUserResponseDto();
        responseDto.setId(appUser.getId());
        responseDto.setUserName(appUser.getUserName());
        responseDto.setEmail(appUser.getEmail());
        responseDto.setPassword(appUser.getPassword());
        return responseDto;
    }

}
