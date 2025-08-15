package com.ubbackend.services;

import com.ubbackend.DTOs.CreateAccessCodeDTO;

import java.util.Optional;

public interface AccessCodeService {
    Optional<Long> generateAccessCode(CreateAccessCodeDTO createAccessCodeDTO) throws Exception;
}
