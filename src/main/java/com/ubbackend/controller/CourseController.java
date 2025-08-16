package com.ubbackend.controller;

import com.ubbackend.DTOs.CourseDTO;
import com.ubbackend.DTOs.CourseRecursionDTO;
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
    public ResponseEntity<CourseEntity> getCourseById(@PathVariable Long id) {
        if(courseService.getCourse(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourse(id).get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
    public ResponseEntity<CourseEntity> deleteCourse(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/course/update")
    public ResponseEntity<CourseEntity> updateCourse(@RequestBody CourseEntity courseEntity) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/course/add/student")
    public ResponseEntity<CourseEntity> addStudentToCourse(@RequestBody NewStudentDTO newStudentDTO) throws Exception {

        Optional<CourseEntity> courseExisting = courseService.newStudent(newStudentDTO);

        return courseExisting
                .map(courseEntity -> ResponseEntity.status(HttpStatus.OK).body(courseEntity))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
