package com.ubbackend.controller;

import com.ubbackend.DTO.AccessCodeCreatedDTO;
import com.ubbackend.DTO.AccessCodeResponseDTO;
import com.ubbackend.model.AccessCodeEntity;
import com.ubbackend.services.AccessCodeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
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

    @Operation(
            summary = "fetch all Access Codes",
            description = "This method return all Generated Access Codes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of Access Codes",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            type = "array",
                                            implementation = AccessCodeResponseDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    )
            }
    )
    @GetMapping("/codes")
    public ResponseEntity<List<AccessCodeEntity>> getAccessCodes() {
        return ResponseEntity.status(HttpStatus.OK).body(accessCodeService.getAllAccessCode());
    }

    @Operation(
            summary = "generate Access Code",
            description = "This method generates one Access Code and returns it",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "The Access Code was generated and returned successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            type = "int",
                                            implementation = Long.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The Access Code could not be generated, please check if the user is present",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    )
            }
    )
    @PostMapping("/access-code")
    public ResponseEntity<String> generateAccessCode(@RequestBody AccessCodeCreatedDTO accessCodeCreatedDTO) throws Exception {

        Optional<Long> accessCodeEntity = accessCodeService.generateAccessCode(accessCodeCreatedDTO);
        return accessCodeEntity
                .map(aLong -> ResponseEntity.status(HttpStatus.CREATED).body(aLong.toString()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(
            summary = "generate Access Code",
            description = "This method generates one Access Code and returns it",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The Access Code was cancelled successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            type = "String",
                                            implementation = AccessCodeResponseDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The Access Code was not be found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/codes/{id}")
    public ResponseEntity<?> cancelAccessCode(@PathVariable Long id) throws Exception {
        Optional<AccessCodeEntity> accessCodeEntity = accessCodeService.cancelAccessCode(id);
        if(accessCodeEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(accessCodeEntity.get());
    }
}
