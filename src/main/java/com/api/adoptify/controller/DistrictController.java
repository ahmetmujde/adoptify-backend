package com.api.adoptify.controller;


import com.api.adoptify.entity.District;
import com.api.adoptify.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/districts")
public class DistrictController {

    private DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping
    public ResponseEntity<List<District>> getDistrictsByCity(@RequestParam("cityId") Long cityId) {
        return ResponseEntity.ok(districtService.getDistrictsByCity(cityId));
    }
}