package com.api.adoptify.service;

import com.api.adoptify.dto.response.CityResponseDto;
import com.api.adoptify.entity.City;
import com.api.adoptify.repository.CityRepository;
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

public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    public void initializeCityServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldGetAllCitiesWhenCitiesExist() {
        // Arrange
        City city1 = new City();
        city1.setId(1L);
        city1.setCityName("City 1");

        City city2 = new City();
        city2.setId(2L);
        city2.setCityName("City 2");

        List<City> mockCities = Arrays.asList(city1, city2);
        when(cityRepository.findAll()).thenReturn(mockCities);

        // Act
        List<CityResponseDto> result = cityService.getAllCities();

        // Assert
        assertEquals(2, result.size());
        assertEquals("City 1", result.get(0).getCityName());
        assertEquals("City 2", result.get(1).getCityName());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    public void shouldGetAllCitiesWhenCitiesIfNotExist() {
        // Arrange
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<CityResponseDto> result = cityService.getAllCities();

        // Assert
        assertTrue(result.isEmpty());
        verify(cityRepository, times(1)).findAll();
    }
}
