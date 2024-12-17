package com.api.adoptify.repository;

import com.api.adoptify.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GenderRepository extends JpaRepository<Gender,Long> {

}