package com.ubbackend.repository;

import com.ubbackend.model.AccessCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessCodeRepository extends JpaRepository<AccessCodeEntity, Long> {
    Optional<AccessCodeEntity> findByCode(Long code);
}
