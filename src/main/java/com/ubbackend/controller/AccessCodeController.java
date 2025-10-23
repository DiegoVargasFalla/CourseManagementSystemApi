package com.ubbackend.controller;

import com.ubbackend.DTO.AccessCodeCreatedDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
            description = "This method return a list with all Generated Access Codes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of Access Codes",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            type = "array",
                                            implementation = AccessCodeEntity.class
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
                                            implementation = Long.class,
                                            example = "123456",
                                            defaultValue = "123456"
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

        Optional<String> accessCodeEntity = accessCodeService.generateAccessCode(accessCodeCreatedDTO);
        return accessCodeEntity
                .map(codeOk  -> ResponseEntity.status(HttpStatus.CREATED).body(codeOk))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(
            summary = "Cancel Access Code",
            description = "This method cancel Access Code and returns it",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The Access Code was cancelled successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = AccessCodeEntity.class
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
    @PatchMapping("/codes/{code}")
    public ResponseEntity<AccessCodeEntity> cancelAccessCode(@PathVariable Long code) throws Exception {
        Optional<AccessCodeEntity> accessCodeEntity = accessCodeService.cancelAccessCode(code);
        return accessCodeEntity
                .map(codeEntity -> ResponseEntity.status(HttpStatus.OK).body(codeEntity))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
