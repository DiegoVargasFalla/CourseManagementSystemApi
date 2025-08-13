package com.ubbackend.controller;

import com.ubbackend.model.Course;
import com.ubbackend.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(name = "/v1")
public class CourseController {

    public final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourse() {
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(new Course());
    }

    @PostMapping("/create/course")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/curse/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/update/course")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
