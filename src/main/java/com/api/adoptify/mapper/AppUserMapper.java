package com.api.adoptify.mapper;

import com.api.adoptify.dto.request.AppUserRequestDto;
import com.api.adoptify.dto.response.AppUserResponseDto;
import com.api.adoptify.entity.Address;
import com.api.adoptify.entity.AppUser;
import com.api.adoptify.entity.City;
import com.api.adoptify.entity.District;
import com.api.adoptify.entity.Gender;

public class AppUserMapper {

    public static AppUser mapToAppUser(AppUserRequestDto appUserRequestDto, Gender gender, City city, District district) {
        AppUser appUser = new AppUser();
        appUser.setUserName(appUserRequestDto.getUserName());
        appUser.setEmail(appUserRequestDto.getEmail());
        appUser.setPassword(appUserRequestDto.getPassword());
        appUser.setPhoneNumber(appUserRequestDto.getPhoneNumber());
        appUser.setCriminalRecord(appUserRequestDto.getCriminalRecord());
        appUser.setGender(gender);

        // Address Mapping
        Address address = new Address();
        address.setArea(appUserRequestDto.getAddress().getArea());
        address.setCity(city);
        address.setDistrict(district);
        appUser.setAddress(address);

        return appUser;
    }

    public static AppUserResponseDto mapToAppUserResponseDto(AppUser appUser) {
        AppUserResponseDto responseDto = new AppUserResponseDto();
        responseDto.setId(appUser.getId());
        responseDto.setUserName(appUser.getUserName());
        responseDto.setEmail(appUser.getEmail());

        return responseDto;
    }
}
