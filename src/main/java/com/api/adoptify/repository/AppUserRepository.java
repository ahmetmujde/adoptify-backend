package com.api.adoptify.repository;

import com.api.adoptify.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM AppUser u JOIN FETCH u.roles WHERE u.id = :userId")
    Optional<AppUser> findByIdWithRoles(@Param("userId") Long userId);

}
