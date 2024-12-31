package com.api.adoptify.service;

import com.api.adoptify.dto.request.AddressRequestDto;
import com.api.adoptify.dto.request.AppUserRequestDto;
import com.api.adoptify.dto.response.AppUserResponseDto;
import com.api.adoptify.entity.*;
import com.api.adoptify.mapper.AppUserMapper;
import com.api.adoptify.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private GenderRepository genderRepository;

    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserWhenValidRequestGiven() {
        // Arrange
        AppUserRequestDto requestDto = new AppUserRequestDto();
        requestDto.setEmail("hikmet@gmail.com");
        requestDto.setPassword("hikmet123");
        requestDto.setGenderId(1L);

        // Create AddressRequestDto
        AddressRequestDto addressRequestDto = new AddressRequestDto();
        addressRequestDto.setCityId(1L); // Mocked City ID
        addressRequestDto.setDistrictId(2L); // Mocked District ID
        requestDto.setAddress(addressRequestDto);

        // Mock Gender
        Gender mockGender = new Gender();
        mockGender.setId(1L);
        mockGender.setGenderName("Male");

        // Mock City and District
        City mockCity = new City();
        mockCity.setId(1L);
        mockCity.setCityName("Manisa");
        District mockDistrict = new District();
        mockDistrict.setId(2L);
        mockDistrict.setDistrictName("Akhisar");

        // Mock Role
        Role userRole = new Role();
        userRole.setName("hikmet");

        // Mock AppUser
        AppUser mockAppUser = new AppUser();
        mockAppUser.setId(1L);
        mockAppUser.setEmail("hikmet@gmail.com");

        // Define Mocks
        when(appUserRepository.existsByEmail("hikmet@gmail.com")).thenReturn(false);
        when(genderRepository.findById(1L)).thenReturn(Optional.of(mockGender));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(mockCity));
        when(districtRepository.findById(2L)).thenReturn(Optional.of(mockDistrict));
        when(roleRepository.findByName("User")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("hikmet123")).thenReturn("encodedPassword");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(mockAppUser);

        // Act
        AppUserResponseDto responseDto = appUserService.createUser(requestDto);

        // Assert
        assertNotNull(responseDto);
        assertEquals("hikmet@gmail.com", responseDto.getEmail());

        // Verify saved AppUser
        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(userCaptor.capture());
        AppUser capturedUser = userCaptor.getValue();

        assertEquals("hikmet@gmail.com", capturedUser.getEmail());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertTrue(capturedUser.getRoles().contains(userRole));
    }



    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        AppUserRequestDto requestDto = new AppUserRequestDto();
        requestDto.setEmail("hikmet@gmail.com");

        when(appUserRepository.existsByEmail("hikmet@gmail.com")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> appUserService.createUser(requestDto));
    }

    @Test
    void shouldGetAllUsers() {
        // Arrange
        AppUser user1 = new AppUser();
        user1.setId(1L);
        user1.setEmail("hikmet@gmail.com");

        AppUser user2 = new AppUser();
        user2.setId(2L);
        user2.setEmail("zehra@gmail.com");

        List<AppUser> users = Arrays.asList(user1, user2);
        when(appUserRepository.findAll()).thenReturn(users);

        // Act
        List<AppUserResponseDto> responseDtos = appUserService.getUsers();

        // Assert
        assertEquals(2, responseDtos.size());
        verify(appUserRepository).findAll();
    }

    @Test
    void shouldIfThereIsAppUserNameGivenByAppUserEmail() {
        // Arrange
        String email = "zehra@gmail.com";
        AppUser mockUser = new AppUser();
        mockUser.setId(1L);
        mockUser.setUserName("zehra");
        mockUser.setEmail(email);

        when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        String appUserName = appUserService.getAppUserNameByUserEmail(email);

        // Assert
        assertEquals("zehra", appUserName);
    }

    @Test
    void shouldIfNotExistAppUserNameGivenByAppUserEmail() {
        // Arrange
        String email = "nonexistent@gmail.com";

        when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        String result = appUserService.getAppUserNameByUserEmail(email);

        // Assert
        assertNull(result);
    }
    @Test
    void shouldFailWhenPasswordEncodingIsIncorrect() {
        // Arrange
        AppUserRequestDto requestDto = new AppUserRequestDto();
        requestDto.setEmail("hikmet@gmail.com");
        requestDto.setPassword("hikmet123");
        requestDto.setGenderId(1L);

        AddressRequestDto address = new AddressRequestDto();
        address.setCityId(1L);
        address.setDistrictId(1L);
        requestDto.setAddress(address);

        Gender gender = new Gender();
        gender.setId(1L);
        City city = new City();
        city.setId(1L);
        District district = new District();
        district.setId(1L);
        Role userRole = new Role();
        userRole.setName("User");

        when(appUserRepository.existsByEmail("hikmet@gmail.com")).thenReturn(false);
        when(genderRepository.findById(1L)).thenReturn(Optional.of(gender));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(districtRepository.findById(1L)).thenReturn(Optional.of(district));
        when(roleRepository.findByName("User")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("hikmet123")).thenReturn("incorrectPassword"); // An incorrect value was returned

        // Act
        AppUserResponseDto responseDto = appUserService.createUser(requestDto);

        // Assert
        assertNotEquals("incorrectPassword", responseDto.getEmail()); // The test will fail because the password do not match
    }
    @Test
    void shouldFailWhenRoleNotFound() {
        // Arrange
        AppUserRequestDto requestDto = new AppUserRequestDto();
        requestDto.setEmail("hikmet@gmail.com");
        requestDto.setPassword("hikmet123");
        requestDto.setGenderId(1L);

        AddressRequestDto address = new AddressRequestDto();
        address.setCityId(1L);
        address.setDistrictId(1L);
        requestDto.setAddress(address);

        Gender gender = new Gender();
        gender.setId(1L);
        City city = new City();
        city.setId(1L);
        District district = new District();
        district.setId(1L);

        when(appUserRepository.existsByEmail("hikmet@gmail.com")).thenReturn(false);
        when(genderRepository.findById(1L)).thenReturn(Optional.of(gender));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(districtRepository.findById(1L)).thenReturn(Optional.of(district));
        when(roleRepository.findByName("User")).thenReturn(Optional.empty()); // Role is not found

        // Act & Assert
        assertThrows(RuntimeException.class, () -> appUserService.createUser(requestDto)); // The test will fail
    }
    @Test
    void shouldFailWhenCityIdIsInvalid() {
        // Arrange
        AppUserRequestDto requestDto = new AppUserRequestDto();
        requestDto.setEmail("hikmet@gmail.com");
        requestDto.setPassword("hikmet123");
        requestDto.setGenderId(1L);

        AddressRequestDto address = new AddressRequestDto();
        address.setCityId(999L); // Invalid ID
        address.setDistrictId(1L);
        requestDto.setAddress(address);

        Gender gender = new Gender();
        gender.setId(1L);

        when(appUserRepository.existsByEmail("hikmet@gmail.com")).thenReturn(false);
        when(genderRepository.findById(1L)).thenReturn(Optional.of(gender));
        when(cityRepository.findById(999L)).thenReturn(Optional.empty()); // City is not found

        // Act & Assert
        assertThrows(RuntimeException.class, () -> appUserService.createUser(requestDto)); // The test will fail
    }
    @Test
    void shouldFailWhenAppUserNameNotFound() {
        // Arrange
        String email = "hikmet@gmail.com";

        when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        String appUserName = appUserService.getAppUserNameByUserEmail(email);

        // Assert
        assertNotNull(appUserName); // The test will fail because the username is null
    }
    @Test
    void shouldFailWhenUserListIsEmpty() {
        // Arrange
        when(appUserRepository.findAll()).thenReturn(Collections.emptyList()); // An empty list is returned

        // Act
        List<AppUserResponseDto> responseDtos = appUserService.getUsers();

        // Assert
        assertFalse(responseDtos.isEmpty()); // The test will fail because the list is empty
    }
}

