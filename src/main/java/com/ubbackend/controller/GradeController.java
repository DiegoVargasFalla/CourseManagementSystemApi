package com.ubbackend.controller;

import com.ubbackend.DTO.GradeRecursionDTO;
import com.ubbackend.model.GradeEntity;
import com.ubbackend.services.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @DeleteMapping("/grades/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(gradeService.deleteGrade(id));
    }

    @PatchMapping("/grades/{id}/value/{value}")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @PathVariable Float value) {
        Optional<GradeRecursionDTO> gradeExisting = gradeService.updateGrade(id, value);
        if(gradeExisting.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(gradeExisting.get());
    }
}
