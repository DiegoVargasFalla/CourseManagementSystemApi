package com.ubbackend.services;

import com.ubbackend.DTO.AccessCodeCreatedDTO;
import com.ubbackend.model.AccessCodeEntity;

import java.util.List;
import java.util.Optional;

public interface AccessCodeService {
    List<AccessCodeEntity> getAllAccessCode();
    Optional<String> generateAccessCode(AccessCodeCreatedDTO accessCodeCreatedDTO) throws Exception;
    Optional<AccessCodeEntity> cancelAccessCode(Long accessCodeId) throws Exception;
    boolean checkAccessCode(Long code) throws Exception;
}
