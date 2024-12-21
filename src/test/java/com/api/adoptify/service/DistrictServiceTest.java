package com.api.adoptify.service;

import com.api.adoptify.entity.City;
import com.api.adoptify.entity.District;
import com.api.adoptify.repository.DistrictRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class DistrictServiceTest {

    @Mock
    private DistrictRepository districtRepository;

    @InjectMocks
    private DistrictService districtService;

    @BeforeEach
    public void initializeDistrictServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldGetDistrictsByCityWhenDistrictsExist() {
        // Arrange
        Long cityId = 1L;
        City mockCity = new City();
        mockCity.setId(cityId);
        mockCity.setCityName("Mock City");

        List<District> mockDistricts = Arrays.asList(
                new District(1L, "District 1", mockCity),
                new District(2L, "District 2", mockCity)
        );
        when(districtRepository.findByCityId(cityId)).thenReturn(mockDistricts);

        // Act
        List<District> result = districtService.getDistrictsByCity(cityId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("District 1", result.get(0).getDistrictName());
        assertEquals("District 2", result.get(1).getDistrictName());
        verify(districtRepository, times(1)).findByCityId(cityId);
    }

    @Test
    public void shouldGetDistrictsByCityWhenDistrictsIfNotExist() {
        // Arrange
        Long cityId = 2L;
        when(districtRepository.findByCityId(cityId)).thenReturn(Collections.emptyList());

        // Act
        List<District> result = districtService.getDistrictsByCity(cityId);

        // Assert
        assertTrue(result.isEmpty());
        verify(districtRepository, times(1)).findByCityId(cityId);
    }
}
