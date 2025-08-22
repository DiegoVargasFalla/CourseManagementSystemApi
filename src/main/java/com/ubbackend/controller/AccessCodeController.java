package com.ubbackend.controller;

import com.ubbackend.DTO.AccessCodeCreatedDTO;
import com.ubbackend.model.AccessCodeEntity;
import com.ubbackend.services.AccessCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(
        name = "Access code controller",
        description = "Access code controller where all the endpoints are located"
)
@RestController
@RequestMapping("/v1")
public class AccessCodeController {

    private final AccessCodeService accessCodeService;

    public AccessCodeController(AccessCodeService accessCodeService) {
        this.accessCodeService = accessCodeService;
    }

    @GetMapping("/codes")
    public ResponseEntity<List<AccessCodeEntity>> getAccessCodes() {
        return ResponseEntity.status(HttpStatus.OK).body(accessCodeService.getAllAccessCode());
    }

    @PostMapping("/access-code")
    public ResponseEntity<?> generateAccessCode(@RequestBody AccessCodeCreatedDTO accessCodeCreatedDTO) throws Exception {

        Optional<Long> accessCodeEntity = accessCodeService.generateAccessCode(accessCodeCreatedDTO);
        if(accessCodeEntity.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(accessCodeEntity.get().toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PatchMapping("/codes/{id}")
    public ResponseEntity<?> cancelAccessCode(@PathVariable Long id) throws Exception {
        Optional<AccessCodeEntity> accessCodeEntity = accessCodeService.cancelAccessCode(id);
        if(accessCodeEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(accessCodeEntity.get());
    }
}
