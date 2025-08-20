package com.ubbackend.controller;

import com.ubbackend.DTO.CourseDTO;
import com.ubbackend.DTO.CourseRecursionDTO;
import com.ubbackend.DTO.CourseUpdateDTO;
import com.ubbackend.DTO.NewStudentDTO;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.repository.CourseRepository;
import com.ubbackend.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class CourseController {
    public final CourseService courseService;

    public CourseController(CourseService courseService, CourseRepository courseRepository) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseRecursionDTO>> getCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourses());
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseRecursionDTO> getCourseById(@PathVariable Long id) throws Exception {
        Optional<CourseRecursionDTO> courseRecursionDTO = courseService.getCourse(id);
        return courseRecursionDTO
                .map(recursionDTO -> ResponseEntity.status(HttpStatus.OK).body(recursionDTO)).
                orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/courses")
    public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseDTO courseDTO) throws Exception {
        Optional<CourseEntity> courseRecursionDTOExisting = courseService.createCourse(courseDTO);
        return courseRecursionDTOExisting
                .map(courseRecursionDTO -> ResponseEntity.status(HttpStatus.CREATED).body(courseRecursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) throws Exception {
        if(courseService.deleteCourse(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Course successfully deleted");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course not found");
    }

    @PatchMapping("/courses")
    public ResponseEntity<CourseRecursionDTO> updateCourse(@RequestBody CourseUpdateDTO courseUpdateDTO) throws Exception {
        Optional<CourseEntity> courseUpdateExisting = courseService.updateCourse(courseUpdateDTO);

        if(courseUpdateExisting.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CourseRecursionDTO courseRecursionDTO = new CourseRecursionDTO();
        courseRecursionDTO.setId(courseUpdateExisting.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(courseRecursionDTO);
    }

    @PostMapping("/courses/students")
    public ResponseEntity<CourseRecursionDTO> addStudentToCourse(@RequestBody NewStudentDTO newStudentDTO) throws Exception {
        Optional<CourseRecursionDTO> courseRecursionDTOExisting = courseService.newStudent(newStudentDTO);

        return courseRecursionDTOExisting
                .map(courseRecursionDTO -> ResponseEntity.status(HttpStatus.OK).body(courseRecursionDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/courses/{courseId}/students/{studentDni}")
    public ResponseEntity<?> deleteStudentFromCourse(@PathVariable Long courseId, @PathVariable Long studentDni) throws Exception {
        NewStudentDTO newStudentDTO = new NewStudentDTO();
        newStudentDTO.setCourseId(courseId);
        newStudentDTO.setDni(studentDni);

        if(courseService.deleteStudentFromCourse(newStudentDTO)) {
            return ResponseEntity.status(HttpStatus.OK).body("Student has been deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
