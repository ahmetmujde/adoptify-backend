package com.api.adoptify.controller;

import com.api.adoptify.dto.response.CityResponseDto;
import com.api.adoptify.entity.City;
import com.api.adoptify.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    private CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<CityResponseDto>> getAllCities() {
        List<CityResponseDto> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }
}
