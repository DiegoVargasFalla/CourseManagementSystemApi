package com.ubbackend.controller;

import com.ubbackend.DTO.GradeRecursionDTO;
import com.ubbackend.model.GradeEntity;
import com.ubbackend.services.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
        name = "Grade controller",
        description = "Student controller where all the endpoints are located"
)
@RestController
@RequestMapping("/v1")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/grades")
    public ResponseEntity<List<GradeEntity>> getGrades() {
        return ResponseEntity.ok(gradeService.getGrades());
    }

    @Operation(
            summary = "Delete grade",
            description = "Method to remove a grade with the grade id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Grade successfully deleted",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "Info message that grade was deleted"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The grade id doesn't exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/grades/{id}")
    public ResponseEntity<?> deleteNote(
            @Parameter(
                    name = "id",
                    description = "The student id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param ID grade that needs to be removed",
                            allowableValues = {"1", "2", "3"}
                    )
            )
            @PathVariable Long id) {
        if(gradeService.deleteGrade(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Grade deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grade could not be deleted");
    }

    @Operation(
            summary = "Update grade",
            description = "Method to remove a grade with the grade id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Grade successfully update",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "Info message that grade was update"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The grade id doesn't exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PatchMapping("/grades/{id}/value/{value}")
    public ResponseEntity<?> updateNote(
            @Parameter(
                    name = "id",
                    description = "The grade id to update",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param ID grade that needs to be updated",
                            allowableValues = {"1", "2", "3"}
                    )
            )
            @PathVariable Long id,
            @Parameter(
                    name = "Grade value",
                    description = "The grade value to update",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param value to update the previous value",
                            allowableValues = {"1", "2.8", "3", "5.2"}
                    )
            )
            @PathVariable Float value) {
        Optional<GradeRecursionDTO> gradeExisting = gradeService.updateGrade(id, value);
        if(gradeExisting.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(gradeExisting.get());
    }
}
