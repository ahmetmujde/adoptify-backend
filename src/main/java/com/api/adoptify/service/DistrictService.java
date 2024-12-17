package com.api.adoptify.service;


import com.api.adoptify.entity.District;
import com.api.adoptify.repository.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {

    private DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<District> getDistrictsByCity(Long cityId) {
        return districtRepository.findByCityId(cityId);
    }
}
