package com.ubbackend.controller;

import com.ubbackend.DTOs.StudentDTO;
import com.ubbackend.model.CourseEntity;
import com.ubbackend.model.StudentEntity;
import com.ubbackend.services.StudentService;
import com.ubbackend.servicesImpl.StudentServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentEntity>> getStudents() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudents());
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentEntity> getStudent(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(new StudentEntity());
    }

    @PostMapping("/create/student")
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDTO) throws Exception {
        Optional<CourseEntity> courseExisting = studentService.createStudent(studentDTO);
        if(courseExisting.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(courseExisting.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student create failed");
        }
    }

    @DeleteMapping("/delete/student/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/update/student")
    public ResponseEntity<?> updateStudent(@RequestBody StudentEntity studentEntity) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
