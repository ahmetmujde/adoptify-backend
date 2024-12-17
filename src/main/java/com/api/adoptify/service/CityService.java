package com.api.adoptify.service;


import com.api.adoptify.dto.response.CityResponseDto;
import com.api.adoptify.entity.City;
import com.api.adoptify.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    private CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<CityResponseDto> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .map(city -> new CityResponseDto(city.getId(), city.getCityName()))
                .collect(Collectors.toList());
    }
}
