package com.api.adoptify.service;

import com.api.adoptify.dto.request.AppUserRequestDto;
import com.api.adoptify.dto.response.AppUserResponseDto;
import com.api.adoptify.entity.AppUser;
import com.api.adoptify.entity.City;
import com.api.adoptify.entity.District;
import com.api.adoptify.entity.Gender;
import com.api.adoptify.entity.Role;
import com.api.adoptify.mapper.AppUserMapper;
import com.api.adoptify.repository.AppUserRepository;
import com.api.adoptify.repository.CityRepository;
import com.api.adoptify.repository.DistrictRepository;
import com.api.adoptify.repository.GenderRepository;
import com.api.adoptify.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AppUserService  {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final GenderRepository genderRepository;

    public AppUserService(AppUserRepository appUserRepository,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository,
                          CityRepository cityRepository,
                          DistrictRepository districtRepository,
                          GenderRepository genderRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
        this.genderRepository = genderRepository;
    }

    public AppUserResponseDto createUser(AppUserRequestDto appUserRequestDto) {
        // Email kontrolü
        if (appUserRepository.existsByEmail(appUserRequestDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + appUserRequestDto.getEmail());
        }

        // Gender ID kontrolü
        if (appUserRequestDto.getGenderId() == null) {
            throw new IllegalArgumentException("Gender ID must not be null");
        }
        Gender gender = genderRepository.findById(appUserRequestDto.getGenderId())
                .orElseThrow(() -> new RuntimeException("Gender not found with ID: " + appUserRequestDto.getGenderId()));

        // City ID kontrolü
        if (appUserRequestDto.getAddress().getCityId() == null) {
            throw new IllegalArgumentException("City ID must not be null");
        }
        City city = cityRepository.findById(appUserRequestDto.getAddress().getCityId())
                .orElseThrow(() -> new RuntimeException("City not found with ID: " + appUserRequestDto.getAddress().getCityId()));

        // District ID kontrolü
        if (appUserRequestDto.getAddress().getDistrictId() == null) {
            throw new IllegalArgumentException("District ID must not be null");
        }
        District district = districtRepository.findById(appUserRequestDto.getAddress().getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found with ID: " + appUserRequestDto.getAddress().getDistrictId()));

        // USER rolünü bul ve ekle
        Role userRole = roleRepository.findByName("User")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));

        // AppUser oluştur ve kaydet
        AppUser appUser = AppUserMapper.mapToAppUser(appUserRequestDto, gender, city, district);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.getRoles().add(userRole);

        AppUser savedAppUser = appUserRepository.save(appUser);

        return AppUserMapper.mapToAppUserResponseDto(savedAppUser);
    }

    public List<AppUserResponseDto> getUsers() {
        List<AppUser> allAppUsers = appUserRepository.findAll();
        List<AppUserResponseDto> allAppUsersResponseDto = new ArrayList<>();

        for (AppUser appUser : allAppUsers) {
            allAppUsersResponseDto.add(AppUserMapper.mapToAppUserResponseDto(appUser));
        }

        return allAppUsersResponseDto;
    }

    public String getAppUserNameByUserEmail(String appUserEmail) {

        if (Objects.nonNull(appUserEmail)) {
            Optional<AppUser> optAppUser = appUserRepository.findByEmail(appUserEmail);
            if (optAppUser.isPresent()) {
                return optAppUser.get().getUserName().toLowerCase();
            }

        }
        return null;
    }
}
