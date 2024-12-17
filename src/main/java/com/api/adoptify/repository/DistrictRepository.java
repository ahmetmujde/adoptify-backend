package com.api.adoptify.repository;

import com.api.adoptify.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District,Long> {

    Optional<District> findByDistrictName(String districtName);

    List<District> findByCityId(Long cityId);
}