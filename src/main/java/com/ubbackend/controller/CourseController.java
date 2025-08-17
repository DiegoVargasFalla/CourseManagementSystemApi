package com.ubbackend.controller;

import com.ubbackend.DTOs.CourseDTO;
import com.ubbackend.DTOs.CourseRecursionDTO;
import com.ubbackend.DTOs.CourseUpdateDTO;
import com.ubbackend.DTOs.NewStudentDTO;
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

    @GetMapping("/course/{id}")
    public ResponseEntity<CourseRecursionDTO> getCourseById(@PathVariable Long id) throws Exception {
        Optional<CourseRecursionDTO> courseRecursionDTO = courseService.getCourse(id);
        return courseRecursionDTO
                .map(recursionDTO -> ResponseEntity.status(HttpStatus.OK).body(recursionDTO)).
                orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/course/create")
    public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseDTO courseDTO) throws Exception {
        if(courseService.createCourse(courseDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/course/{id}/delete")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) throws Exception {
        if(courseService.deleteCourse(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Course successfully deleted");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Course not found");
    }

    @PatchMapping("/course/update")
    public ResponseEntity<CourseEntity> updateCourse(@RequestBody CourseUpdateDTO courseUpdateDTO) throws Exception {
        Optional<CourseEntity> courseUpdateExisting = courseService.updateCourse(courseUpdateDTO);
        return courseUpdateExisting
                .map(courseEntity -> ResponseEntity.status(HttpStatus.OK).body(courseEntity))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/course/add/student")
    public ResponseEntity<CourseEntity> addStudentToCourse(@RequestBody NewStudentDTO newStudentDTO) throws Exception {
        Optional<CourseEntity> courseExisting = courseService.newStudent(newStudentDTO);

        return courseExisting
                .map(courseEntity -> ResponseEntity.status(HttpStatus.OK).body(courseEntity))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/course/student/delete")
    public ResponseEntity<?> deleteStudentFromCourse(@RequestBody NewStudentDTO newStudentDTO) throws Exception {
        if(courseService.deleteStudentFromCourse(newStudentDTO)) {
            return ResponseEntity.status(HttpStatus.OK).body("Student has been deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
