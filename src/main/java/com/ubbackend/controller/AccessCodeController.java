package com.ubbackend.controller;

import com.ubbackend.DTOs.CreateAccessCodeDTO;
import com.ubbackend.services.AccessCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class AccessCodeController {

    private final AccessCodeService accessCodeService;

    public AccessCodeController(AccessCodeService accessCodeService) {
        this.accessCodeService = accessCodeService;
    }

    @PostMapping("/access-code")
    public ResponseEntity<?> generateAccessCode(@RequestBody CreateAccessCodeDTO createAccessCodeDTO) throws Exception {

        Optional<Long> accessCodeEntity = accessCodeService.generateAccessCode(createAccessCodeDTO);
        if(accessCodeEntity.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(accessCodeEntity.get().toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
